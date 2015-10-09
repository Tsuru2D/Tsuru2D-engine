package com.tsuru2d.engine.lua;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaThread;

public class GlobalContext {
    private static final Globals sGlobals;
    static {
        sGlobals = new Globals();
    }

    public Globals instance() {
        return sGlobals;
        LuaThread.getGlobals()
    }
}
