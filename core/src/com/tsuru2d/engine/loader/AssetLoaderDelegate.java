package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;

/* package */ abstract class AssetLoaderDelegate<T, TRaw> {
    private class LoadedCallbackImpl implements AssetLoaderParameters.LoadedCallback {
        private final AssetID mID;

        public LoadedCallbackImpl(AssetID id) {
            mID = id;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void finishedLoading(AssetManager assetManager, String fileName, Class type) {
            onRawAssetLoaded(mID, assetManager.get(fileName, mRawType));
        }
    }

    protected final AssetLoader mAssetLoader;
    private final AssetPathResolver mPathResolver;
    protected final Class<TRaw> mRawType;

    public AssetLoaderDelegate(AssetLoader assetLoader, AssetPathResolver pathResolver, Class<TRaw> rawType) {
        mAssetLoader = assetLoader;
        mPathResolver = pathResolver;
        mRawType = rawType;
        pathResolver.addObserver(this);
    }

    /* package */ abstract ManagedAsset<T> getAsset(AssetID assetID);
    /* package */ abstract void fillRawAsset(ManagedAsset<T> asset);
    /* package */ abstract void freeAsset(ManagedAsset<T> asset);
    protected abstract AssetID getRawAssetID(AssetID assetID);
    protected abstract void onRawAssetLoaded(AssetID rawAssetID, TRaw value);
    protected abstract void onRawAssetInvalidated(AssetID baseRawAssetID);

    protected void startLoadingRaw(AssetID rawAssetID) {
        mAssetLoader.startLoadingRaw(resolvePath(rawAssetID), mRawType, new LoadedCallbackImpl(rawAssetID));
    }

    protected void finishLoadingRaw(AssetID rawAssetID) {
        mAssetLoader.finishLoadingRaw(resolvePath(rawAssetID));
    }

    protected void unloadRaw(AssetID rawAssetID) {
        mAssetLoader.unloadRaw(resolvePath(rawAssetID));
    }

    private String resolvePath(AssetID rawAssetID) {
        return mPathResolver.resolve(rawAssetID);
    }
}
