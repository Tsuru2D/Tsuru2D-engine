package com.tsuru2d.engine.loader;

import java.io.File;

public interface AssetFinder {
    String findFileName(File parentDir, String name);
}
