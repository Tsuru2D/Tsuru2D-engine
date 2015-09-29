package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaValue;

/* package */ class TextAssetLoaderDelegate extends MultiAssetLoaderDelegate<String, LuaValue> {
    public TextAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaValue.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<String> asset, LuaValue table) {
        String[] path = asset.getAssetID().getPath();
        String key = path[path.length - 1];
        String value = table.get(key).checkjstring();
        asset.setRawAsset(value);
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }
}
