package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaValue;

public abstract class GameAction extends ExposedJavaClass {
    @ExposeToLua(name = "wait")
    public abstract LuaValue luaWait();
}
