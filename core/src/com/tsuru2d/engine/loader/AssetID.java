package com.tsuru2d.engine.loader;

import java.util.Arrays;

/**
 * A key that is used to look up a {@link ManagedAsset}.
 * Game developers can use abstract paths such as
 * {@code R.localization.en.text.chapter1.scene1.hello} instead of
 * {@code "localization.en.text/chapter1/scene1.lua:hello"}. The asset
 * IDs will be mapped to actual file paths on disk by
 * {@link AssetPathResolver}.
 */
public class AssetID {
    private static final String[] EMPTY_PATH = new String[0];
    public static final AssetID SOUND = new AssetID(AssetType.SOUND, EMPTY_PATH);
    public static final AssetID MUSIC = new AssetID(AssetType.MUSIC, EMPTY_PATH);
    public static final AssetID VOICE = new AssetID(AssetType.VOICE, EMPTY_PATH);
    public static final AssetID TEXT = new AssetID(AssetType.TEXT, EMPTY_PATH);

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

    // TODO: getParent() and getChild() are very memory inefficient.
    // It would be best to create a tree of asset IDs, which are reused
    // for multiple objects. getPath() would then walk up the tree and
    // create the path array on demand. This is reasonable since getPath()
    // will not be called too often, but getChild() will be called
    // multiple times while creating a single asset ID.
    public AssetID getParent() {
        String[] path = new String[mPath.length - 1];
        System.arraycopy(mPath, 0, path, 0, path.length);
        return new AssetID(mType, path);
    }

    public AssetID getChild(String subPath) {
        String[] path = new String[mPath.length + 1];
        System.arraycopy(mPath, 0, path, 0, mPath.length);
        path[path.length - 1] = subPath;
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
        StringBuilder sb = new StringBuilder();
        sb.append("R.");
        sb.append(mType.name().toLowerCase());
        for (String pathSection : mPath) {
            sb.append('.');
            sb.append(pathSection);
        }
        return sb.toString();
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
