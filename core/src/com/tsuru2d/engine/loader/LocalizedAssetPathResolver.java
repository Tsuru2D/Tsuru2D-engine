package com.tsuru2d.engine.loader;

import com.badlogic.gdx.utils.Array;

import java.io.File;
import java.util.Map;

public class LocalizedAssetPathResolver implements AssetPathResolver {
    private final FileFinder mFileFinder;
    private final String mLanguagesPath;
    private final Map<AssetType, String> mTypePathMap;
    private final Array<AssetLoaderDelegate<?, ?>> mObservers;
    private String mLanguageCode;

    public LocalizedAssetPathResolver(FileFinder fileFinder, String languagesPath, Map<AssetType, String> typePathMap) {
        mFileFinder = fileFinder;
        mLanguagesPath = languagesPath;
        mTypePathMap = typePathMap;
        mObservers = new Array<AssetLoaderDelegate<?, ?>>();
    }

    @Override
    public String resolve(AssetID rawAssetID) {
        File path = getLocalizedRootPath(rawAssetID.getType());
        String[] subPaths = rawAssetID.getPath();
        for (int i = 0; i < subPaths.length - 1; ++i) {
            path = new File(path, subPaths[i]);
        }
        path = new File(path, mFileFinder.findFileName(path, subPaths[subPaths.length - 1]));
        return path.getPath();
    }

    @Override
    public void addObserver(AssetLoaderDelegate<?, ?> delegate) {
        mObservers.add(delegate);
    }

    @Override
    public void removeObserver(AssetLoaderDelegate<?, ?> delegate) {
        mObservers.removeValue(delegate, true);
    }

    public String getLanguage() {
        return mLanguageCode;
    }

    public void setLanguage(String languageCode) {
        if (languageCode.equals(mLanguageCode)) {
            return;
        }

        mLanguageCode = languageCode;
        // TODO: Invalidate observers
    }

    private File getPathForLanguage() {
        return new File(mLanguagesPath, mLanguageCode);
    }

    private File getLocalizedRootPath(AssetType type) {
        return new File(getPathForLanguage(), mTypePathMap.get(type));
    }
}
