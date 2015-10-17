package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class LuaAssetIDLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable root = new LuaTable();
        AssetType[] assetTypes = AssetType.values();
        for (AssetType assetType : assetTypes) {
            String key = assetType.name().toLowerCase();
            LuaAssetID value = new LuaAssetID(AssetID.getRoot(assetType));
            root.set(key, value);
        }
        env.set("R", root);
        return LuaValue.NIL;
    }
}
