package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

/* package */ abstract class AssetLoaderDelegate<T, TRaw> {
    private class LoadedCallbackImpl implements AssetLoaderParameters.LoadedCallback {
        private final AssetID mRawAssetID;

        public LoadedCallbackImpl(AssetID id) {
            mRawAssetID = id;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
            onRawAssetLoaded(mRawAssetID, assetManager.get(fileName, mRawType));
        }
    }

    protected final AssetLoader mAssetLoader;
    protected final Class<TRaw> mRawType;

    public AssetLoaderDelegate(AssetLoader assetLoader, Class<TRaw> rawType) {
        mAssetLoader = assetLoader;
        mRawType = rawType;
    }

    /* package */ abstract ManagedAsset<T> getAsset(AssetID assetID);
    /* package */ abstract void fillRawAsset(ManagedAsset<T> asset);
    /* package */ abstract void freeAsset(ManagedAsset<T> asset);
    protected abstract AssetID getRawAssetID(AssetID assetID);
    protected abstract void onRawAssetLoaded(AssetID rawAssetID, TRaw value);
    protected abstract void onRawAssetInvalidated(AssetID baseRawAssetID);

    protected void startLoadingRaw(AssetID rawAssetID) {
        mAssetLoader.startLoadingRaw(rawAssetID, mRawType, new LoadedCallbackImpl(rawAssetID));
    }

    protected void startReloadingRaw(AssetID rawAssetID) {
        unloadRaw(rawAssetID);
        startLoadingRaw(rawAssetID);
    }

    protected void finishLoadingRaw(AssetID rawAssetID) {
        mAssetLoader.finishLoadingRaw(rawAssetID);
    }

    protected void unloadRaw(AssetID rawAssetID) {
        mAssetLoader.unloadRaw(rawAssetID);
    }
}
