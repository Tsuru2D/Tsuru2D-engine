package com.tsuru2d.engine.gameapi;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;

public class AsyncGameAction extends GameAction {
    private final Globals mGlobals;

    public AsyncGameAction(Globals globals) {
        mGlobals = globals;
    }

    @Override
    public LuaValue luaWait() {
        return mGlobals.get("coroutine").get("wait").call();
    }
}
