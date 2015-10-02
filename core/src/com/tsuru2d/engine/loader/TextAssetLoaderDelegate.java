package com.tsuru2d.engine.loader;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/* package */ class TextAssetLoaderDelegate extends MultiAssetLoaderDelegate<String, LuaValue> {
    public TextAssetLoaderDelegate(AssetLoader assetLoader) {
        super(assetLoader, LuaValue.class);
    }

    @Override
    protected void consumeRawAsset(ManagedAsset<String> asset, LuaValue table) {
        String key = asset.getAssetID().getName();
        String value = table.get(key).checkjstring();
        asset.setRawAsset(value);
    }

    @Override
    protected AssetID getRawAssetID(AssetID assetID) {
        return assetID.getParent();
    }

    @Override
    protected AssetLoaderParameters<LuaValue> getParameters() {
        Globals globals = getAssetLoader().getGame().getLuaContext();
        LuaTable env = new LuaTable();
        return new LuaFileLoader.LuaFileParameter(globals, env);
    }
}
