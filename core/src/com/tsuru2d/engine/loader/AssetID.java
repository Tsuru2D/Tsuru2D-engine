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

    public boolean isParentOrEqual(AssetID other) {
        if (mType != other.mType) return false;
        if (mPath.length > other.mPath.length) return false;
        for (int i = 0; i < mPath.length; ++i) {
            if (!mPath[i].equals(other.mPath[i])) {
                return false;
            }
        }
        return true;
    }

    public AssetID getParent() {
        String[] path = new String[mPath.length - 1];
        System.arraycopy(mPath, 0, path, 0, path.length);
        return new AssetID(mType, path);
    }

    /* package */ AssetID checkType(AssetType expectedType) throws AssetTypeMismatchException {
        if (mType != expectedType) {
            throw new AssetTypeMismatchException(
                "Attempting to load asset of type " + expectedType +
                " with asset ID: " + toString());
        }
        return this;
    }

    @Override
    public String toString() {
        StringBuilder pathBuilder = new StringBuilder();
        for (String pathSection : mPath) {
            pathBuilder.append(pathSection);
            pathBuilder.append('.');
        }
        pathBuilder.setLength(pathBuilder.length() - 1);
        return String.format("AssetID{Type=%s, Path=%s}", mType.name(), pathBuilder.toString());
    }

    @Override
    public int hashCode() {
        return mType.hashCode() * 31 + Arrays.hashCode(mPath);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof AssetID)) return false;
        AssetID other = (AssetID)obj;
        if (mType != other.mType) return false;
        if (!Arrays.equals(mPath, other.mPath)) return false;
        return true;
    }
}
