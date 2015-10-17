package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.tsuru2d.engine.model.GameMetadataInfo;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetLoader implements Disposable {
    private final RawAssetLoader mRawAssetLoader;
    private final Map<AssetType, AssetLoaderDelegate<?, ?>> mLoaderDelegates;
    private final Pool<ManagedAsset<?>> mAssetPool;

    public AssetLoader(RawAssetLoader rawAssetLoader) {
        mRawAssetLoader = rawAssetLoader;
        mLoaderDelegates = new HashMap<AssetType, AssetLoaderDelegate<?, ?>>();
        mLoaderDelegates.put(AssetType.SOUND, new SingleAssetLoaderDelegate<Sound>(this, Sound.class));
        mLoaderDelegates.put(AssetType.MUSIC, new SingleAssetLoaderDelegate<Music>(this, Music.class));
        mLoaderDelegates.put(AssetType.VOICE, new SingleAssetLoaderDelegate<Sound>(this, Sound.class));
        mLoaderDelegates.put(AssetType.IMAGE, new SingleAssetLoaderDelegate<Texture>(this, Texture.class));
        mLoaderDelegates.put(AssetType.TEXT, new TextAssetLoaderDelegate(this));
        mLoaderDelegates.put(AssetType.SCREEN, new SingleAssetLoaderDelegate<LuaValue>(this, LuaValue.class));
        mLoaderDelegates.put(AssetType.SCENE, new SingleAssetLoaderDelegate<LuaValue>(this, LuaValue.class));
        mLoaderDelegates.put(AssetType.CHARACTER, new SingleAssetLoaderDelegate<LuaValue>(this, LuaValue.class));
        // TODO: Add other types
        mAssetPool = new Pool<ManagedAsset<?>>() {
            @Override
            protected ManagedAsset<?> newObject() {
                return new ManagedAsset<Object>();
            }
        };
    }

    public String getLanguage() {
        return mRawAssetLoader.getLanguage();
    }

    public void setLanguage(String languageCode) {
        List<AssetID> invalidatedIDs = mRawAssetLoader.setLanguage(languageCode);
        for (AssetID baseRawAssetID : invalidatedIDs) {
            AssetLoaderDelegate<?, ?> delegate = mLoaderDelegates.get(baseRawAssetID.getType());
            delegate.onRawAssetInvalidated(baseRawAssetID);
        }
    }

    public ManagedAsset<Sound> getSound(AssetID id) {
        return getAsset(id.checkType(AssetType.SOUND));
    }

    public ManagedAsset<Music> getMusic(AssetID id) {
        return getAsset(id.checkType(AssetType.MUSIC));
    }

    public ManagedAsset<Sound> getVoice(AssetID id) {
        return getAsset(id.checkType(AssetType.VOICE));
    }

    public ManagedAsset<Texture> getImage(AssetID id) {
        return getAsset(id.checkType(AssetType.IMAGE));
    }

    public ManagedAsset<String> getText(AssetID id) {
        return getAsset(id.checkType(AssetType.TEXT));
    }

    public LuaTable getScreen(AssetID id) {
        return ((LuaValue)getAsset(id.checkType(AssetType.SCREEN)).get()).checktable();
    }

    public LuaTable getScene(AssetID id) {
        return ((LuaValue)getAsset(id.checkType(AssetType.SCENE)).get()).checktable();
    }

    public LuaTable getCharacter(AssetID id) {
        return ((LuaValue)getAsset(id.checkType(AssetType.CHARACTER)).get()).checktable();
    }

    public GameMetadataInfo getMetadata() {
        return mRawAssetLoader.getMetadata();
    }

    @SuppressWarnings("unchecked")
    private <T> AssetLoaderDelegate<T, ?> getDelegate(AssetID id) {
        return (AssetLoaderDelegate<T, ?>)mLoaderDelegates.get(id.getType());
    }

    @SuppressWarnings("unchecked")
    /* package */ <T> ManagedAsset<T> obtainAssetFromPool() {
        return (ManagedAsset<T>)mAssetPool.obtain();
    }

    /* package */ <T> void releaseAssetToPool(ManagedAsset<T> asset) {
        mAssetPool.free(asset);
    }

    /* package */ <T> void startLoadingRaw(AssetID rawAssetID, Class<T> type, AssetLoaderParameters<T> params) {
        mRawAssetLoader.startLoadingRaw(rawAssetID, type, params);
    }

    /* package */ void finishLoadingRaw(AssetID rawAssetID) {
        mRawAssetLoader.finishLoadingRaw(rawAssetID);
    }

    /* package */ void unloadRaw(AssetID rawAssetID) {
        mRawAssetLoader.unloadRaw(rawAssetID);
    }

    /**
     * Gets an asset with the specified asset ID, and increment its
     * reference counter. Make sure to call {@link #freeAsset(ManagedAsset)}
     * once you are done using the asset.
     * @param id The managed ID used to search for the asset.
     */
    private <T> ManagedAsset<T> getAsset(AssetID id) {
        AssetLoaderDelegate<T, ?> delegate = getDelegate(id);
        ManagedAsset<T> asset = delegate.getAsset(id);
        asset.incrementRef();
        return asset;
    }

    /**
     * Decrements the reference counter of the specified asset.
     * If there are no more references to it, its resources will
     * be deallocated. Do NOT use the asset after calling this method!
     * Also, if you have registered any {@link AssetObserver} objects,
     * unregister them before calling this method.
     * @param asset The asset to free.
     */
    public <T> void freeAsset(ManagedAsset<T> asset) {
        if (asset.decrementRef() == 0) {
            AssetLoaderDelegate<T, ?> delegate = getDelegate(asset.getAssetID());
            delegate.freeAsset(asset);
        }
    }

    public void update() {
        mRawAssetLoader.update();
    }

    @Override
    public void dispose() {
        mRawAssetLoader.dispose();
    }
}
