package com.tsuru2d.engine.loader;

import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* package */ abstract class MultiAssetLoaderDelegate<T, TRaw> extends AssetLoaderDelegate<T, TRaw> {
    private final Map<AssetID, TRaw> mRawAssets;
    private final Map<AssetID, Array<ManagedAsset<T>>> mAssets;

    public MultiAssetLoaderDelegate(AssetLoader assetLoader, Class<TRaw> rawType) {
        super(assetLoader, rawType);
        mRawAssets = new HashMap<AssetID, TRaw>();
        mAssets = new HashMap<AssetID, Array<ManagedAsset<T>>>();
    }

    @Override
    /* package */ ManagedAsset<T> getAsset(AssetID assetID) {
        AssetID rawAssetID = getRawAssetID(assetID);
        ManagedAsset<T> asset = getCachedAsset(assetID, rawAssetID);
        if (asset == null) {
            asset = createNewAsset(assetID, rawAssetID);
        }
        return asset;
    }

    @Override
    /* package */ void fillRawAsset(ManagedAsset<T> asset) {
        AssetID rawAssetID = getRawAssetID(asset.getAssetID());
        TRaw table = mRawAssets.get(rawAssetID);
        if (table == null) {
            finishLoadingRaw(rawAssetID);
        }
    }

    @Override
    /* package */ void freeAsset(ManagedAsset<T> asset) {
        AssetID rawAssetID = getRawAssetID(asset.getAssetID());
        Array<ManagedAsset<T>> mContainer = mAssets.get(rawAssetID);
        mContainer.removeValue(asset, true);
        if (mContainer.size == 0) {
            mRawAssets.remove(rawAssetID);
            mAssets.remove(rawAssetID);
            unloadRaw(rawAssetID);
        }
    }

    @Override
    protected void onRawAssetLoaded(AssetID rawAssetID, TRaw value) {
        mRawAssets.put(rawAssetID, value);
        Array<ManagedAsset<T>> container = mAssets.get(rawAssetID);
        for (ManagedAsset<T> asset : container) {
            consumeRawAsset(asset, value);
        }
    }

    @Override
    protected void onRawAssetInvalidated(AssetID baseRawAssetID) {
        // Use mAssets and not mRawAssets here, because for each key in
        // mRawAssets, there must be a key in mAssets, but the reverse
        // is not true when there are assets that are not finished loading.
        Iterator<AssetID> it = mAssets.keySet().iterator();
        while (it.hasNext()) {
            AssetID rawAssetID = it.next();
            if (baseRawAssetID.isParentOrEqual(rawAssetID)) {
                it.remove();
                Array<ManagedAsset<T>> assets = mAssets.get(rawAssetID);
                for (ManagedAsset<T> asset : assets) {
                    asset.invalidate();
                }
            }
            startReloadingRaw(rawAssetID);
        }
    }

    private ManagedAsset<T> getCachedAsset(AssetID assetID, AssetID rawAssetID) {
        for (ManagedAsset<T> asset : mAssets.get(rawAssetID)) {
            if (asset.getAssetID().equals(assetID)) {
                return asset;
            }
        }
        return null;
    }

    private ManagedAsset<T> createNewAsset(AssetID assetID, AssetID rawAssetID) {
        Array<ManagedAsset<T>> container = mAssets.get(rawAssetID);
        ManagedAsset<T> asset = mAssetLoader.createAsset();
        asset.setAssetID(assetID);
        asset.setLoader(this);
        if (container == null) {
            container = new Array<ManagedAsset<T>>();
            mAssets.put(rawAssetID, container);
            startLoadingRaw(rawAssetID);
        } else {
            TRaw table = mRawAssets.get(rawAssetID);
            // The asset cannot possibly have any observers attached at
            // this point, so this action is safe
            consumeRawAsset(asset, table);
        }
        container.add(asset);
        return asset;
    }

    protected abstract void consumeRawAsset(ManagedAsset<T> asset, TRaw value);
}
