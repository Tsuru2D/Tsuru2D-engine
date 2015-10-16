package com.tsuru2d.engine.gameapi;

import org.luaj.vm2.LuaValue;

public class InstantGameAction extends GameAction {
    public static final InstantGameAction EMPTY = new InstantGameAction();

    private final LuaValue mValue;

    private InstantGameAction() {
        this(LuaValue.NIL);
    }

    public InstantGameAction(LuaValue value) {
        mValue = value;
    }

    @Override
    public LuaValue luaWait() {
        return mValue;
    }
}
