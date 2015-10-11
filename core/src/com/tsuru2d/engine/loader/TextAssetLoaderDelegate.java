package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import org.luaj.vm2.LuaTable;

/* package */ class TextAssetLoaderDelegate extends MultiAssetLoaderDelegate<String, LuaTable> {
    public TextAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaTable.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<String> asset, LuaTable table) {
        String key = asset.getAssetID().getName();
        String value = table.get(key).checkjstring();
        asset.setRawAsset(value);
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }

    @Override
    protected AssetLoaderParameters<LuaTable> getParameters() {
        return new LuaFileLoader.LuaFileParameter();
    }
}
