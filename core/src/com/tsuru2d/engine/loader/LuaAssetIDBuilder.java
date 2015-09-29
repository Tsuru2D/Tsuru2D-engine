package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class LuaAssetIDBuilder extends LuaTable {
    private static class LuaAssetIDBuilderRoot extends LuaAssetIDBuilder {
        private final AssetType mAssetType;

        private LuaAssetIDBuilderRoot(AssetType assetType) {
            mAssetType = assetType;
        }

        @Override
        protected AssetType fillAssetID(String[] path, int index) {
            return mAssetType;
        }

        @Override
        public AssetID getAssetID() {
            throw new UnsupportedOperationException();
        }
    }

    private static final TwoArgFunction CHILD_INDEX_FUNC = new TwoArgFunction() {
        @Override
        public LuaValue call(LuaValue table, LuaValue key) {
            LuaAssetIDBuilder builder = (LuaAssetIDBuilder)table;
            return new LuaAssetIDBuilder(builder, key.checkjstring());
        }
    };

    private static final LuaAssetIDBuilderRoot VOICE = new LuaAssetIDBuilderRoot(AssetType.VOICE);
    private static final LuaAssetIDBuilderRoot SOUND = new LuaAssetIDBuilderRoot(AssetType.SOUND);
    private static final LuaAssetIDBuilderRoot MUSIC = new LuaAssetIDBuilderRoot(AssetType.MUSIC);
    private static final LuaAssetIDBuilderRoot TEXT = new LuaAssetIDBuilderRoot(AssetType.TEXT);
    // TODO: Add the rest...

    private final LuaAssetIDBuilder mParent;
    private final String mSubPath;
    private final int mDepth;

    private LuaAssetIDBuilder() {
        this(null, null);
    }

    private LuaAssetIDBuilder(LuaAssetIDBuilder parent, String subPath) {
        mParent = parent;
        mSubPath = subPath;
        mDepth = (parent == null) ? 0 : parent.mDepth + 1;
        setmetatable(this);
        set(LuaValue.INDEX, CHILD_INDEX_FUNC);
    }

    protected AssetType fillAssetID(String[] path, int index) {
        AssetType assetType = mParent.fillAssetID(path, index - 1);
        path[index] = mSubPath;
        return assetType;
    }

    public AssetID getAssetID() {
        String[] path = new String[mDepth];
        AssetType type = fillAssetID(path, mDepth - 1);
        return new AssetID(type, path);
    }

    public static void install(LuaTable environment) {
        LuaTable root = new LuaTable();
        root.set("sound", SOUND);
        root.set("music", MUSIC);
        root.set("voice", VOICE);
        root.set("text", TEXT);
        environment.set("R", root);
    }
}
