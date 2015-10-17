package com.tsuru2d.engine.gameapi;

import org.luaj.vm2.*;

public class AsyncGameAction extends GameAction {
    private final Globals mGlobals;
    private final LuaThread mThread;
    private volatile Varargs mValue;

    public AsyncGameAction(Globals globals, LuaThread gameThread) {
        mGlobals = globals;
        mThread = gameThread;
    }

    @Override
    public synchronized void luaFinish(Varargs args) {
        mValue = args;
        mThread.resume(args);
    }

    @Override
    public Varargs luaWait() {
        // This uses the double-checked locking pattern to
        // avoid a synchronized block in the case where the
        // value has already been set
        Varargs value = mValue;
        if (value == null) {
            synchronized (this) {
                value = mValue;
                if (value == null) {
                    value = mValue = mGlobals.yield(LuaValue.NIL);
                }
            }
        }
        return value;
    }
}
