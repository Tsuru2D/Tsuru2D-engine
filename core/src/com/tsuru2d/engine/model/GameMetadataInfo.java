package com.tsuru2d.engine.model;

import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.MetadataLoader;

import java.util.Map;

public class GameMetadataInfo {
    public String mPackageName;
    public String mVersionName;
    public int mVersionCode;
    public String mLanguageCode;
    public String mLanguageName;
    public MetadataLoader.Resolution mResolution;
    public Map<AssetType, String> mAssetDirs;
    public AssetID mTitle;
    public AssetID mAuthor;
    public AssetID mMainScreen;
    public AssetID mGameScreen;
}
