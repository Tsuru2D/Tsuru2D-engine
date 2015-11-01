package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/* package */ class SkinAssetLoaderDelegate extends MultiAssetLoaderDelegate<LuaTable, LuaValue> {
    public SkinAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaValue.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<LuaTable> asset, LuaValue table) {
        String key = asset.getAssetID().getName();
        LuaTable value = table.checktable().get(key).checktable();
        asset.setRawAsset(value);
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }
}
