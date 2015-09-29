package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Pool;
import com.tsuru2d.engine.model.CharacterInfo;
import com.tsuru2d.engine.model.SceneInfo;
import com.tsuru2d.engine.model.ScreenInfo;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private final AssetManager mAssetManager;
    private final Map<AssetID, String> mResolvePathCache;
    private final AssetPathResolver mPathResolver;
    private final Map<AssetType, AssetLoaderDelegate<?, ?>> mLoaderDelegates;
    private final Pool<ManagedAsset<?>> mAssetPool;

    public AssetLoader(FileHandleResolver handleResolver, AssetPathResolver pathResolver) {
        mAssetManager = new AssetManager(handleResolver);
        mAssetManager.setLoader(LuaValue.class, new LuaFileLoader(handleResolver));
        mResolvePathCache = new HashMap<AssetID, String>();
        mPathResolver = pathResolver;
        mPathResolver.setAssetLoader(this);
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

    /* package */ <T> void startLoadingRaw(AssetID rawAssetID, Class<T> type, AssetLoaderParameters.LoadedCallback callback) {
        String path = mPathResolver.resolve(rawAssetID);
        mResolvePathCache.put(rawAssetID, path);
        AssetLoaderParameters<T> params = new AssetLoaderParameters<T>();
        params.loadedCallback = callback;
        mAssetManager.load(path, type, params);
    }

    /* package */ void finishLoadingRaw(AssetID rawAssetID) {
        mAssetManager.finishLoadingAsset(mResolvePathCache.get(rawAssetID));
    }

    /* package */ void unloadRaw(AssetID rawAssetID) {
        String path = mResolvePathCache.remove(rawAssetID);
        mAssetManager.unload(path);
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
