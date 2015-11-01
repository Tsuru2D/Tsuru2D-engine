package com.tsuru2d.engine.loader;

import com.tsuru2d.engine.loader.exception.AssetNotFoundException;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/* package */ class TextAssetLoaderDelegate extends MultiAssetLoaderDelegate<String, LuaTable> {
    public TextAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaTable.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<String> asset, LuaTable table) {
        String key = asset.getAssetID().getName();
        LuaValue value = table.get(key);
        if (value.isnil()) {
            throw new AssetNotFoundException("Cannot find asset: " + asset.getAssetID());
        }
        asset.setRawAsset(value.checkjstring());
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }
}
