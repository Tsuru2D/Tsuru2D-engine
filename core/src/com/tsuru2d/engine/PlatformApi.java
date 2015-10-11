package com.tsuru2d.engine;

import com.tsuru2d.engine.io.FileManager;
import com.tsuru2d.engine.io.NetManager;
import com.tsuru2d.engine.loader.RawAssetLoader;

public class PlatformApi {
    private final FileManager mFileManager;
    private final NetManager mNetManager;
    private final RawAssetLoader mRawAssetLoader;

    public PlatformApi(FileManager fileManager, NetManager netManager, RawAssetLoader rawAssetLoader) {
        mFileManager = fileManager;
        mNetManager = netManager;
        mRawAssetLoader = rawAssetLoader;
    }

    public FileManager getFileManager() {
        return mFileManager;
    }

    public NetManager getNetManager() {
        return mNetManager;
    }

    public RawAssetLoader getRawAssetLoader() {
        return mRawAssetLoader;
    }
}
