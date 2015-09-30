package com.tsuru2d.engine.loader;

import java.io.File;
import java.util.Map;

public class AssetPathResolver {
    private final AssetFinder mAssetFinder;
    private final String mLanguagesPath;
    private final Map<AssetType, String> mTypePathMap;
    private AssetLoader mAssetLoader;
    private String mLanguageCode;

    public AssetPathResolver(AssetFinder assetFinder, String languagesPath, Map<AssetType, String> typePathMap) {
        mAssetFinder = assetFinder;
        mLanguagesPath = languagesPath;
        mTypePathMap = typePathMap;
    }

    public String resolve(AssetID rawAssetID) {
        File path = getLocalizedRootPath(rawAssetID.getType());
        String[] subPaths = rawAssetID.getPath();
        for (int i = 0; i < subPaths.length - 1; ++i) {
            path = new File(path, subPaths[i]);
        }
        path = new File(path, mAssetFinder.findFileName(path, subPaths[subPaths.length - 1]));
        return path.getPath();
    }

    public void setAssetLoader(AssetLoader loader) {
        mAssetLoader = loader;
    }

    public String getLanguage() {
        return mLanguageCode;
    }

    public void setLanguage(String languageCode) {
        if (languageCode.equals(mLanguageCode)) {
            return;
        }

        mLanguageCode = languageCode;
        mAssetLoader.invalidatePath(AssetID.TEXT);
        // TODO: Invalidate observers
    }

    private File getPathForLanguage() {
        return new File(mLanguagesPath, mLanguageCode);
    }

    private File getLocalizedRootPath(AssetType type) {
        return new File(getPathForLanguage(), mTypePathMap.get(type));
    }
}
