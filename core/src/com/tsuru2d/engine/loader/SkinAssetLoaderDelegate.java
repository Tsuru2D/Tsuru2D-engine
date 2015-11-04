package com.tsuru2d.engine.loader;

import com.tsuru2d.engine.loader.exception.AssetNotFoundException;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/* package */ class SkinAssetLoaderDelegate extends MultiAssetLoaderDelegate<LuaTable, LuaValue> {
    public SkinAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaValue.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<LuaTable> asset, LuaValue table) {
        String key = asset.getAssetID().getName();
        LuaValue value = table.get(key);
        if (value.isnil()) {
            throw new AssetNotFoundException("Cannot find asset: " + asset.getAssetID());
        }
        asset.setRawAsset(value.checktable());
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }
}
