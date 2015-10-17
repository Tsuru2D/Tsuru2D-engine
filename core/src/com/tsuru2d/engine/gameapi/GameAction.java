package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.Varargs;

public abstract class GameAction extends ExposedJavaClass {
    @ExposeToLua(name = "finish")
    public abstract void luaFinish(Varargs args);

    @ExposeToLua(name = "wait")
    public abstract Varargs luaWait();
}
