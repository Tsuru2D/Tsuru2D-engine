package com.tsuru2d.engine.loader;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.HashMap;
import java.util.Map;

public class AssetLoader {
    private String mLanguageCode;
    private Map<AssetType, Array<ManagedAsset<?>>> mLoadedAssets;
    private Pool<ManagedAsset<?>> mAssetPool;

    public AssetLoader() {
        mLoadedAssets = new HashMap<AssetType, Array<ManagedAsset<?>>>();
        mAssetPool = new Pool<ManagedAsset<?>>() {
            @Override
            protected ManagedAsset<?> newObject() {
                return new ManagedAsset<Object>(AssetLoader.this);
            }
        };
    }

    public String getLanguage() {
        return mLanguageCode;
    }

    public void setLanguage(String languageCode) {
        if (languageCode.equals(mLanguageCode)) {
            return;
        }

        mLanguageCode = languageCode;
        // TODO: Re-load some stuff here

        // Only invalidate text strings for now
        Array<ManagedAsset<?>> mTextRes = mLoadedAssets.get(AssetType.TEXT);
        for (ManagedAsset<?> asset : mTextRes) {
            asset.invalidate();
        }
    }

    public ManagedAsset<Sound> getVoice(AssetID id) {
        id.checkType(AssetType.VOICE);
        return getAsset(id, Sound.class);
    }

    public ManagedAsset<Sound> getSound(AssetID id) {
        id.checkType(AssetType.MUSIC);
        return getAsset(id, Sound.class);
    }

    public ManagedAsset<Music> getMusic(AssetID id) {
        id.checkType(AssetType.SOUND);
        return getAsset(id, Music.class);
    }

    public ManagedAsset<String> getText(AssetID id) {
        id.checkType(AssetType.TEXT);
        return getAsset(id, String.class);
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
        asset.setRawValue(getAssetRaw(id, type), type);
        return asset;
    }

    /* package */ <T> T getAssetRaw(AssetID id, Class<T> type) {
        // TODO: Implement
        return null;
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
            mLoadedAssets.get(asset.getAssetID().getType()).removeValue(asset, true);
            mAssetPool.free(asset);
        }
    }
}
