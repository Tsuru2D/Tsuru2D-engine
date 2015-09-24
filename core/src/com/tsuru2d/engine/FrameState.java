package com.tsuru2d.engine;

import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class FrameState {
    @ExposeToLua
    public Object createObject(AssetID id, LuaTable values) {
        return null;
    }

    @ExposeToLua
    public Object getObject(AssetID id) {
        return null;
    }

    @ExposeToLua
    public void deleteObject(AssetID id) {

    }

    @ExposeToLua
    public void playSound(AssetID id) {

    }

    @ExposeToLua
    public void playMusic(AssetID id) {

    }

    @ExposeToLua
    public void setCharacter(AssetID id) {

    }

    @ExposeToLua
    public void setText(AssetID id) {

    }

    @ExposeToLua
    public void setBackgroundImage(AssetID id) {

    }

    @ExposeToLua
    public void transformCamera(LuaTable transformationInfo) {

    }
}
