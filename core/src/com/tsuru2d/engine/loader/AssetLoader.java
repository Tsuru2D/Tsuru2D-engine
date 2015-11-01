package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.tsuru2d.engine.gameapi.GameScene;
import com.tsuru2d.engine.model.GameMetadataInfo;
import com.tsuru2d.engine.util.Xlog;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AssetLoader implements Disposable {
    private static final boolean DEBUG_LEAKS = true;

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
        mLoaderDelegates.put(AssetType.SKIN, new SkinAssetLoaderDelegate(this));
        mLoaderDelegates.put(AssetType.SCREEN, new SingleAssetLoaderDelegate<LuaValue>(this, LuaValue.class));
        mLoaderDelegates.put(AssetType.SCENE, new SingleAssetLoaderDelegate<LuaValue>(this, LuaValue.class));
        mLoaderDelegates.put(AssetType.OBJECT, new SingleAssetLoaderDelegate<LuaValue>(this, LuaValue.class));
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
        return getAsset(AssetType.SOUND, id);
    }

    public ManagedAsset<Music> getMusic(AssetID id) {
        return getAsset(AssetType.MUSIC, id);
    }

    public ManagedAsset<Sound> getVoice(AssetID id) {
        return getAsset(AssetType.VOICE, id);
    }

    public ManagedAsset<Texture> getImage(AssetID id) {
        return getAsset(AssetType.IMAGE, id);
    }

    public ManagedAsset<String> getText(AssetID id) {
        return getAsset(AssetType.TEXT, id);
    }

    public ManagedAsset<LuaTable> getSkin(AssetID id) {
        return getAsset(AssetType.SKIN, id);
    }

    public LuaTable getScreen(AssetID id) {
        ManagedAsset<LuaValue> screenWrapper = getAsset(AssetType.SCREEN, id);
        LuaTable screen = screenWrapper.get().checktable();
        freeAsset(screenWrapper);
        return screen;
    }

    public GameScene getScene(AssetID id) {
        ManagedAsset<LuaValue> sceneFuncWrapper = getAsset(AssetType.SCENE, id);
        LuaFunction sceneFunc = sceneFuncWrapper.get().checkfunction();
        GameScene scene = GameScene.loadFunc(sceneFunc);
        freeAsset(sceneFuncWrapper);
        return scene;
    }

    public LuaTable getObject(AssetID id) {
        ManagedAsset<LuaValue> objectWrapper = getAsset(AssetType.OBJECT, id);
        LuaTable object = objectWrapper.get().checktable();
        freeAsset(objectWrapper);
        return object;
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
     * @param assetType The expected type of the asset.
     * @param id The managed ID used to search for the asset.
     */
    public <T> ManagedAsset<T> getAsset(AssetType assetType, AssetID id) {
        id.checkType(assetType);
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
        if (DEBUG_LEAKS) {
            for (AssetLoaderDelegate<?, ?> delegate : mLoaderDelegates.values()) {
                Array<AssetID> loadedIDs = delegate.getLoadedAssetIDs();
                if (loadedIDs.size > 0) {
                    for (AssetID leakedID : loadedIDs) {
                        Xlog.e("Asset leaked: %s", leakedID);
                    }
                }
            }
        }
        mRawAssetLoader.dispose();
    }
}
