package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;

/* package */ class StyleAssetLoaderDelegate extends MultiAssetLoaderDelegate<LuaTable, LuaTable> {
    public StyleAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaTable.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<LuaTable> asset, LuaTable table) {
        String key = asset.getAssetID().getName();
        LuaTable value = table.get(key).checktable();
        asset.setRawAsset(value);
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }
}
