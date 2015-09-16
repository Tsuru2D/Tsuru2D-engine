package com.tsuru2d.engine;

import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class GameObject {
    @ExposeToLua
    public void transform(LuaTable transformInfo) {

    }

    @ExposeToLua
    public void setState(AssetIdentifier identifier) {

    }
}
