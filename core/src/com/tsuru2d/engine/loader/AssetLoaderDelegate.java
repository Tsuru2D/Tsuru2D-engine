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
        public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
            onRawAssetLoaded(mRawAssetID, assetManager.get(fileName, mRawType));
        }
    }

    private final AssetLoader mAssetLoader;
    private final Class<TRaw> mRawType;

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

    protected AssetLoaderParameters<TRaw> getParameters() {
        // Sub-classes must override this method if they use a
        // non-standard parameter type, or if they want to add
        // additional parameters
        return LoaderParamFactory.get(mRawType);
    }

    protected ManagedAsset<T> obtainAssetFromPool() {
        return mAssetLoader.obtainAssetFromPool();
    }

    protected void releaseAssetToPool(ManagedAsset<T> asset) {
        mAssetLoader.releaseAssetToPool(asset);
    }

    protected void startLoadingRaw(AssetID rawAssetID) {
        AssetLoaderParameters<TRaw> params = getParameters();
        params.loadedCallback = new LoadedCallbackImpl(rawAssetID);
        mAssetLoader.startLoadingRaw(rawAssetID, mRawType, params);
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
