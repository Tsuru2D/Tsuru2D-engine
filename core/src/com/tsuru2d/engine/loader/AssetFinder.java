package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.util.Map;

public abstract class AssetFinder implements FileHandleResolver {
    public interface AssetPathListener {
        void onAssetPathInvalidated(AssetID baseRawAssetID);
    }

    private AssetPathListener mPathListener;
    private Map<AssetType, String> mTypePathMap;
    private String mLanguageCode;

    public void setPathListener(AssetPathListener listener) {
        mPathListener = listener;
    }

    public void setLanguage(String languageCode) {
        if (languageCode.equals(mLanguageCode)) {
            return;
        }

        mLanguageCode = languageCode;

        if (mPathListener != null) {
            // TODO
            mPathListener.onAssetPathInvalidated(AssetID.TEXT);
        }
    }

    public String getLanguage() {
        return mLanguageCode;
    }

    public FileHandle getMetadataHandle() {
        return resolve("metadata.lua");
    }

    public abstract String findPath(AssetID rawAssetID);
    protected abstract void onLanguageChanged();
}
