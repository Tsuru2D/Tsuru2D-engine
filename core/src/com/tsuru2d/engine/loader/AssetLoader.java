package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Pool;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.model.CharacterInfo;
import com.tsuru2d.engine.model.SceneInfo;
import com.tsuru2d.engine.model.ScreenInfo;
import com.tsuru2d.engine.util.Xlog;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private final EngineMain mGame;
    private final AssetFinder mAssetFinder;
    private final AssetManager mAssetManager;
    private final Map<AssetID, String> mResolvePathCache;
    private final Map<AssetType, AssetLoaderDelegate<?, ?>> mLoaderDelegates;
    private final Pool<ManagedAsset<?>> mAssetPool;

    public AssetLoader(EngineMain game, AssetFinder assetFinder) {
        mGame = game;
        mAssetFinder = assetFinder;
        mAssetManager = new AssetManager(assetFinder);
        mAssetManager.setLoader(LuaValue.class, new LuaFileLoader(assetFinder));
        mResolvePathCache = new HashMap<AssetID, String>();
        mLoaderDelegates = new HashMap<AssetType, AssetLoaderDelegate<?, ?>>();
        mLoaderDelegates.put(AssetType.SOUND, new SingleAssetLoaderDelegate<Sound>(this, Sound.class));
        mLoaderDelegates.put(AssetType.MUSIC, new SingleAssetLoaderDelegate<Music>(this, Music.class));
        mLoaderDelegates.put(AssetType.VOICE, new SingleAssetLoaderDelegate<Sound>(this, Sound.class));
        mLoaderDelegates.put(AssetType.TEXT, new TextAssetLoaderDelegate(this));
        // TODO: Add other types
        mAssetPool = new Pool<ManagedAsset<?>>() {
            @Override
            protected ManagedAsset<?> newObject() {
                return new ManagedAsset<Object>();
            }
        };
    }

    public EngineMain getGame() {
        return mGame;
    }

    public ManagedAsset<Sound> getSound(AssetID id) {
        return getAsset(id.checkType(AssetType.MUSIC));
    }

    public ManagedAsset<Music> getMusic(AssetID id) {
        return getAsset(id.checkType(AssetType.SOUND));
    }

    public ManagedAsset<Sound> getVoice(AssetID id) {
        return getAsset(id.checkType(AssetType.VOICE));
    }

    public ManagedAsset<String> getText(AssetID id) {
        return getAsset(id.checkType(AssetType.TEXT));
    }

    public ManagedAsset<ScreenInfo> getScreen(AssetID id) {
        return getAsset(id.checkType(AssetType.SCREEN));
    }

    public ManagedAsset<SceneInfo> getScene(AssetID id) {
        return getAsset(id.checkType(AssetType.SCENE));
    }

    public ManagedAsset<CharacterInfo> getCharacter(AssetID id) {
        return getAsset(id.checkType(AssetType.CHARACTER));
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
        String path = mAssetFinder.findPath(rawAssetID);
        mResolvePathCache.put(rawAssetID, path);
        mAssetManager.load(path, type, params);
        Xlog.d("Started loading asset: %s", rawAssetID);
    }

    /* package */ void finishLoadingRaw(AssetID rawAssetID) {
        mAssetManager.finishLoadingAsset(mResolvePathCache.get(rawAssetID));
    }

    /* package */ void unloadRaw(AssetID rawAssetID) {
        String path = mResolvePathCache.remove(rawAssetID);
        mAssetManager.unload(path);
        Xlog.d("Unloading asset: %s", rawAssetID);
    }

    /* package */ void invalidatePath(AssetID baseRawAssetID) {
        for (AssetLoaderDelegate<?, ?> delegate : mLoaderDelegates.values()) {
            delegate.onRawAssetInvalidated(baseRawAssetID);
        }
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
        mAssetManager.update();
    }

    public void update(int millis) {
        mAssetManager.update(millis);
    }
}
