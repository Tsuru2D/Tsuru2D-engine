package com.tsuru2d.engine.loader;

import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.loader.exception.AssetTypeMismatchException;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

/**
 * A key that is used to look up a {@link ManagedAsset}.
 * Game developers can use abstract paths such as
 * {@code R.text.chapter1.scene1.hello} instead of
 * {@code "text/chapter1/scene1.lua:hello"}. The asset
 * IDs will be mapped to a file asset ID by {@link AssetLoaderDelegate},
 * which will then be loaded by an instance of {@link RawAssetLoader}.
 */
public abstract class AssetID {
    public static final AssetID SOUND = new RootAssetID(AssetType.SOUND);
    public static final AssetID MUSIC = new RootAssetID(AssetType.MUSIC);
    public static final AssetID VOICE = new RootAssetID(AssetType.VOICE);
    public static final AssetID IMAGE = new RootAssetID(AssetType.IMAGE);
    public static final AssetID TEXT = new RootAssetID(AssetType.TEXT);
    public static final AssetID SCREEN = new RootAssetID(AssetType.SCREEN);
    public static final AssetID SCENE = new RootAssetID(AssetType.SCENE);
    public static final AssetID CHARACTER = new RootAssetID(AssetType.CHARACTER);

    private static class RootAssetID extends AssetID {
        private final AssetType mType;

        private RootAssetID(AssetType type) {
            mType = type;
        }

        @Override
        public AssetType getType() {
            return mType;
        }

        @Override
        public AssetID getParent() {
            return null;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public int hashCode() {
            return mType.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof RootAssetID)) return false;
            RootAssetID other = (RootAssetID)obj;
            return mType == other.mType;
        }

        @Override
        public String toString() {
            return "R." + mType.name().toLowerCase();
        }
    }

    private static class ChildAssetID extends AssetID {
        private final AssetID mParent;
        private final String mName;

        private ChildAssetID(AssetID parent, String path) {
            mParent = parent;
            mName = path;
        }

        public AssetType getType() {
            return mParent.getType();
        }

        @Override
        public AssetID getParent() {
            return mParent;
        }

        @Override
        public String getName() {
            return mName;
        }

        @Override
        public String toString() {
            return mParent.toString() + "." + mName;
        }
    }

    private Map<String, WeakReference<AssetID>> mChildren;

    private AssetID() { }

    private void fillPath(Array<String> path) {
        AssetID parent = getParent();
        if (parent != null) {
            parent.fillPath(path);
            path.add(getName());
        }
    }

    public String[] getPath() {
        Array<String> path = new Array<String>(String.class);
        fillPath(path);
        return path.shrink();
    }

    public boolean isParentOrEqual(AssetID other) {
        for (AssetID obj = this; obj != null; obj = obj.getParent()) {
            if (obj == other) {
                return true;
            }
        }
        return false;
    }

    public AssetID getChild(String subPath) {
        WeakReference<AssetID> childRef = null;
        if (mChildren == null) {
            mChildren = new HashMap<String, WeakReference<AssetID>>();
        } else {
            childRef = mChildren.get(subPath);
        }
        AssetID child;
        if (childRef == null || (child = childRef.get()) == null) {
            child = new ChildAssetID(this, subPath);
            mChildren.put(subPath, new WeakReference<AssetID>(child));
        }
        return child;
    }

    /* package */ AssetID checkType(AssetType expectedType) throws AssetTypeMismatchException {
        if (getType() != expectedType) {
            throw new AssetTypeMismatchException(
                "Attempting to load asset of type " + expectedType +
                " with asset ID: " + toString());
        }
        return this;
    }

    public abstract AssetType getType();
    public abstract AssetID getParent();
    public abstract String getName();
    public abstract String toString();

    // There is no need to override hashCode() and equals(),
    // since asset IDs are cached based on value. If
    // any two asset IDs exist at the same time and
    // have the same values, they must refer to the
    // same object.
}
