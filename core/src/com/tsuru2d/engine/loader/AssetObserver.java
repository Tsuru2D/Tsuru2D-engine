package com.tsuru2d.engine.loader;

public interface AssetObserver<T> {
    void onAssetUpdated(ManagedAsset<T> asset);
}
