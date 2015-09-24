package com.tsuru2d.engine.loader;

import java.util.Arrays;

/**
 * A key that is used to look up a {@link ManagedAsset}.
 */
public class AssetID {
    private final AssetType mType;
    private final String[] mPath;

    public AssetID(AssetType type, String[] path) {
        mType = type;
        mPath = path;
    }

    public AssetType getType() {
        return mType;
    }

    public String[] getPath() {
        return mPath;
    }

    /* package */ void checkType(AssetType expectedType) throws AssetTypeMismatchException {
        if (mType != expectedType) {
            throw new AssetTypeMismatchException(
                "Attempting to load asset of type " + expectedType +
                " using asset ID of type " + mType + ": " + Arrays.toString(mPath));
        }
    }
}
