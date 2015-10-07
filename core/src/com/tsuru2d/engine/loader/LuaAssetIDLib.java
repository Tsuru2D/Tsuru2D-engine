package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class LuaAssetIDLib extends TwoArgFunction {
    private static final LuaAssetID SOUND = new LuaAssetID(AssetID.SOUND);
    private static final LuaAssetID MUSIC = new LuaAssetID(AssetID.MUSIC);
    private static final LuaAssetID VOICE = new LuaAssetID(AssetID.VOICE);
    private static final LuaAssetID TEXT = new LuaAssetID(AssetID.TEXT);

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
