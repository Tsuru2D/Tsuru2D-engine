package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

public class LuaAssetIDLib extends TwoArgFunction {
    private static final LuaTable ASSETID_METATABLE;
    static {
        ASSETID_METATABLE = new LuaTable();
        ASSETID_METATABLE.set(LuaValue.INDEX, new TwoArgFunction() {
            @Override
            public LuaValue call(LuaValue obj, LuaValue key) {
                AssetID id = (AssetID)obj.checkuserdata();
                return new NewAssetIDBuilder(id.getChild(key.checkjstring()));
            }
        });
        ASSETID_METATABLE.set(LuaValue.TOSTRING, new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                return LuaValue.valueOf(arg.checkuserdata().toString());
            }
        });
    }

    private static final NewAssetIDBuilder SOUND = new NewAssetIDBuilder(AssetID.SOUND);
    private static final NewAssetIDBuilder MUSIC = new NewAssetIDBuilder(AssetID.MUSIC);
    private static final NewAssetIDBuilder VOICE = new NewAssetIDBuilder(AssetID.VOICE);
    private static final NewAssetIDBuilder TEXT = new NewAssetIDBuilder(AssetID.TEXT);

    public static class NewAssetIDBuilder extends LuaUserdata {
        public NewAssetIDBuilder(AssetID id) {
            super(id, ASSETID_METATABLE);
        }
    }

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable root = new LuaTable();
        root.set("sound", SOUND);
        root.set("music", MUSIC);
        root.set("voice", VOICE);
        root.set("text", TEXT);
        env.set("R", root);
        return LuaValue.NIL;
    }
}
