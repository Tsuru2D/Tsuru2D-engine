package com.tsuru2d.engine.model;

import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.MetadataLoader;

import java.util.Map;

public class MetadataInfo {
    public String mPackageName;
    public String mVersionName;
    public int mVersionCode;
    public MetadataLoader.Resolution mResolution;
    public Map<AssetType, String> mAssetDirs;
    public AssetID mTitle;
    public AssetID mAuthor;
    public AssetID mInitialScreen;
}
