package com.tsuru2d.engine.loader;

import com.badlogic.gdx.utils.Array;

/* package */ class SingleAssetLoaderDelegate<T> extends AssetLoaderDelegate<T, T> {
    private final Array<ManagedAsset<T>> mAssets;

    public SingleAssetLoaderDelegate(AssetLoader assetLoader, Class<T> rawType) {
        super(assetLoader, rawType);
        mAssets = new Array<ManagedAsset<T>>();
    }

    @Override
    /* package */ ManagedAsset<T> getAsset(AssetID assetID) {
        ManagedAsset<T> asset = getCachedAsset(assetID);
        if (asset == null) {
            asset = createNewAsset(assetID);
        }
        return asset;
    }

    @Override
    /* package */ void fillRawAsset(ManagedAsset<T> asset) {
        finishLoadingRaw(getRawAssetID(asset.getAssetID()));
    }

    @Override
    /* package */ void freeAsset(ManagedAsset<T> asset) {
        mAssets.removeValue(asset, true);
        unloadRaw(getRawAssetID(asset.getAssetID()));
        releaseAssetToPool(asset);
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID;
    }

    @Override
    protected void onRawAssetLoaded(AssetID rawAssetID, T value) {
        ManagedAsset<T> asset = getCachedAsset(rawAssetID);
        asset.setRawAsset(value);
    }

    @Override
    protected void onRawAssetInvalidated(AssetID baseRawAssetID) {
        for (ManagedAsset<T> asset : mAssets) {
            AssetID rawAssetID = getRawAssetID(asset.getAssetID());
            if (baseRawAssetID.isParentOrEqual(rawAssetID)) {
                asset.invalidate();
                startReloadingRaw(rawAssetID);
            }
        }
    }

    private ManagedAsset<T> createNewAsset(AssetID assetID) {
        ManagedAsset<T> asset = obtainAssetFromPool();
        asset.setAssetID(assetID);
        asset.setLoader(this);
        startLoadingRaw(getRawAssetID(assetID));
        mAssets.add(asset);
        return asset;
    }

    private ManagedAsset<T> getCachedAsset(AssetID assetID) {
        for (ManagedAsset<T> asset : mAssets) {
            if (asset.getAssetID().equals(assetID)) {
                return asset;
            }
        }
        return null;
    }
}
