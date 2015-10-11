package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.tsuru2d.engine.model.MetadataInfo;

import java.util.List;

public interface RawAssetLoader {
    <T> void startLoadingRaw(AssetID rawAssetID, Class<T> type, AssetLoaderParameters<T> params);
    void finishLoadingRaw(AssetID rawAssetID);
    void unloadRaw(AssetID rawAssetID);
    void update();
    MetadataInfo getMetadata();
    List<AssetID> setLanguage(String languageCode);
    String getLanguage();
}
