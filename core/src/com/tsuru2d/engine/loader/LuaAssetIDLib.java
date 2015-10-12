package com.tsuru2d.engine.loader;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public class LuaAssetIDLib extends TwoArgFunction {
    private static final LuaAssetID SOUND = new LuaAssetID(AssetID.SOUND);
    private static final LuaAssetID MUSIC = new LuaAssetID(AssetID.MUSIC);
    private static final LuaAssetID VOICE = new LuaAssetID(AssetID.VOICE);
    private static final LuaAssetID IMAGE = new LuaAssetID(AssetID.IMAGE);
    private static final LuaAssetID TEXT = new LuaAssetID(AssetID.TEXT);
    private static final LuaAssetID SCREEN = new LuaAssetID(AssetID.SCREEN);
    private static final LuaAssetID SCENE = new LuaAssetID(AssetID.SCENE);
    private static final LuaAssetID CHARACTER = new LuaAssetID(AssetID.CHARACTER);

    @Override
    public LuaValue call(LuaValue modname, LuaValue env) {
        LuaTable root = new LuaTable();
        root.set("sound", SOUND);
        root.set("music", MUSIC);
        root.set("voice", VOICE);
        root.set("image", IMAGE);
        root.set("text", TEXT);
        root.set("screen", SCREEN);
        root.set("scene", SCENE);
        root.set("character", CHARACTER);
        env.set("R", root);
        return LuaValue.NIL;
    }
}
