package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class LuaAssetID extends LuaUserdata {
    private static final LuaTable ASSETID_METATABLE;
    static {
        ASSETID_METATABLE = new LuaTable();
        ASSETID_METATABLE.set(LuaValue.INDEX, new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue obj, LuaValue key) {
                AssetID id = (AssetID)obj.checkuserdata();
                return new LuaAssetID(id.getChild(key.checkjstring()));
            }
        });
        ASSETID_METATABLE.set(LuaValue.TOSTRING, new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(arg.checkuserdata().toString());
            }
        });
    }

    public LuaAssetID(AssetID id) {
        super(id, ASSETID_METATABLE);
    }

    @Override
    public AssetID userdata() {
        return (AssetID)m_instance;
    }
}
