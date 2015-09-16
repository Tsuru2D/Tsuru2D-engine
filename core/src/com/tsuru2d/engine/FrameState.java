package com.tsuru2d.engine;

import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class FrameState {
    @ExposeToLua
    public GameObject createObject(AssetIdentifier id, LuaTable values) {
        return null;
    }

    @ExposeToLua
    public GameObject getObject(AssetIdentifier id) {
        return null;
    }

    @ExposeToLua
    public void deleteObject(AssetIdentifier id) {

    }

    @ExposeToLua
    public void playSound(AssetIdentifier id) {

    }

    @ExposeToLua
    public void playMusic(AssetIdentifier id) {

    }

    @ExposeToLua
    public void setCharacter(AssetIdentifier id) {

    }

    @ExposeToLua
    public void setText(AssetIdentifier id) {

    }

    @ExposeToLua
    public void setBackgroundImage(AssetIdentifier id) {

    }

    @ExposeToLua
    public void transformCamera(LuaTable transformationInfo) {

    }
}
