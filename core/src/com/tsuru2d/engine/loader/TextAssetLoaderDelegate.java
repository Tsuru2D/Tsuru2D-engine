package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaValue;

/* package */ class TextAssetLoaderDelegate extends MultiAssetLoaderDelegate<String, LuaValue> {
    public TextAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaValue.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<String> asset, LuaValue table) {
        String key = asset.getAssetID().getPath()[-1];
        String value = table.get(key).checkjstring();
        asset.setRawAsset(value);
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }
}
