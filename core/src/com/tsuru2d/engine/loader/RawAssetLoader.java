package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.tsuru2d.engine.model.GameMetadataInfo;

import java.util.List;

public interface RawAssetLoader {
    <T> void startLoadingRaw(AssetID rawAssetID, Class<T> type, AssetLoaderParameters<T> params);
    void finishLoadingRaw(AssetID rawAssetID);
    void unloadRaw(AssetID rawAssetID);
    void update();
    GameMetadataInfo getMetadata();
    List<String> getAvailableLanguages();
    List<AssetID> setLanguage(String languageCode);
    String getLanguage();
}
