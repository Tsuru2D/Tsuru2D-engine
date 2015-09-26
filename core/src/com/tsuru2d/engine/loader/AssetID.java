package com.tsuru2d.engine.loader;

/**
 * A key that is used to look up a {@link ManagedAsset}.
 */
public class AssetID {
    private final AssetType mType;
    private final String mPath;

    public AssetID(AssetType type, String path) {
        mType = type;
        mPath = path;
    }

    public AssetType getType() {
        return mType;
    }

    public String getPath() {
        return mPath;
    }

    /* package */ void checkType(AssetType expectedType) throws AssetTypeMismatchException {
        if (mType != expectedType) {
            throw new AssetTypeMismatchException(
                "Attempting to load asset of type " + expectedType +
                " using asset ID of type " + mType + ": " + mPath);
        }
    }

    @Override
    public int hashCode() {
        return mType.hashCode() * 31 + mPath.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AssetID)) return false;
        AssetID other = (AssetID)obj;
        if (mType != other.mType) return false;
        if (!mPath.equals(other.mPath)) return false;
        return true;
    }
}
