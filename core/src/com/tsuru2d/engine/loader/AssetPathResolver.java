package com.tsuru2d.engine.loader;

public interface AssetPathResolver {
    String resolve(AssetID rawAssetID);
    void addObserver(AssetLoaderDelegate<?, ?> delegate);
    void removeObserver(AssetLoaderDelegate<?, ?> delegate);
}
