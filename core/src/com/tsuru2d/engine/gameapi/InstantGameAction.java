package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

public class InstantGameAction extends GameAction {
    public static final InstantGameAction EMPTY = new InstantGameAction(LuaValue.NIL);

    private final Varargs mValue;

    public InstantGameAction(Varargs args) {
        mValue = args;
    }

    @Override
    public void luaFinish(Varargs args) {
        throw new UnsupportedOperationException("Cannot finish() an instant action");
    }

    @Override
    @ExposeToLua(name = "wait")
    public Varargs luaWait() {
        return mValue;
    }
}
