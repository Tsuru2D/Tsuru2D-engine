package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.tsuru2d.engine.model.CharacterInfo;
import com.tsuru2d.engine.model.SceneInfo;
import com.tsuru2d.engine.model.ScreenInfo;

import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private String mLanguageCode;
    private AssetManager mAssetManager;
    private Map<AssetType, Array<ManagedAsset<?>>> mLoadedAssets;
    private Pool<ManagedAsset<?>> mAssetPool;

    public AssetLoader(FileHandleResolver handleResolver) {
        mAssetManager = new AssetManager(handleResolver);
        mLoadedAssets = new HashMap<AssetType, Array<ManagedAsset<?>>>();
        mAssetPool = new Pool<ManagedAsset<?>>() {
            @Override
            protected ManagedAsset<?> newObject() {
                return new ManagedAsset<Object>(AssetLoader.this);
            }
        };
    }

    public String getLanguage() {
        // TODO: Also remove
        return mLanguageCode;
    }

    public void setLanguage(String languageCode) {
        // TODO: This method should be moved to some external class.
        // That method will swap the FileHandleResolver of this class, and
        // all file paths that changed should be invalidated.
        if (languageCode.equals(mLanguageCode)) {
            return;
        }

        mLanguageCode = languageCode;
        // TODO: Re-load some stuff here
        // TODO: Update assetmanager

        // Only invalidate text strings for now
        Array<ManagedAsset<?>> mTextRes = mLoadedAssets.get(AssetType.TEXT);
        for (ManagedAsset<?> asset : mTextRes) {
            asset.invalidate();
        }
    }

    public ManagedAsset<Sound> getSound(AssetID id) {
        id.checkType(AssetType.MUSIC);
        return getAsset(id, Sound.class);
    }

    public ManagedAsset<Music> getMusic(AssetID id) {
        id.checkType(AssetType.SOUND);
        return getAsset(id, Music.class);
    }

    public ManagedAsset<Sound> getVoice(AssetID id) {
        id.checkType(AssetType.VOICE);
        return getAsset(id, Sound.class);
    }

    public ManagedAsset<String> getText(AssetID id) {
        id.checkType(AssetType.TEXT);
        return getAsset(id, String.class);
    }

    public ManagedAsset<ScreenInfo> getScreen(AssetID id) {
        id.checkType(AssetType.SCREEN);
        return null;
    }

    public ManagedAsset<SceneInfo> getScene(AssetID id) {
        id.checkType(AssetType.SCENE);
        return null;
    }

    public ManagedAsset<CharacterInfo> getCharacter(AssetID id) {
        id.checkType(AssetType.CHARACTER);
        return null;
    }

    /**
     * Gets an asset with the specified asset ID, and increment its
     * reference counter. Make sure to call {@link #freeAsset(ManagedAsset)}
     * once you are done using the asset.
     * @param id The ID used to search for the asset.
     * @param type The type of the raw asset.
     */
    private <T> ManagedAsset<T> getAsset(AssetID id, Class<T> type) {
        ManagedAsset<T> asset = loadCachedAsset(id);
        if (asset == null) {
            asset = loadNewAsset(id, type);
        }
        asset.incrementRef();
        return asset;
    }

    @SuppressWarnings("unchecked")
    private <T> ManagedAsset<T> loadCachedAsset(AssetID id) {
        Array<ManagedAsset<?>> assetsOfSameType = mLoadedAssets.get(id.getType());
        for (ManagedAsset<?> asset : assetsOfSameType) {
            if (asset.getAssetID().equals(id)) {
                return (ManagedAsset<T>)asset;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> ManagedAsset<T> loadNewAsset(AssetID id, Class<T> type) {
        ManagedAsset<T> asset = (ManagedAsset<T>)mAssetPool.obtain();
        asset.setAssetID(id);
        // Pre-load the asset.
        switch (id.getType()) {
        case SOUND:
        case MUSIC:
        case VOICE:
            mAssetManager.load(id.getPath(), type);
        default:
            // TODO: Custom load here
        }
        return asset;
    }

    /* package */ <T> T getAssetRaw(AssetID id) {
        switch (id.getType()) {
        case SOUND:
        case MUSIC:
        case VOICE:
            mAssetManager.finishLoadingAsset(id.getPath());
            return mAssetManager.get(id.getPath());
        default:
            // TODO: Custom load here
            return null;
        }
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
            AssetID id = asset.getAssetID();
            mAssetManager.unload(id.getPath());
            mLoadedAssets.get(id.getType()).removeValue(asset, true);
            mAssetPool.free(asset);
        }
    }

    public void update() {
        mAssetManager.update();
    }

    public void update(int millis) {
        mAssetManager.update(millis);
    }
}
