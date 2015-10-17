package com.tsuru2d.engine.gameapi;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class GameFrame {
    private final String mFrameID;
    private final LuaFunction mFrameFunc;

    public GameFrame(String frameID, LuaFunction frameFunc) {
        mFrameID = frameID;
        mFrameFunc = frameFunc;
    }

    public String getFrameID() {
        return mFrameID;
    }

    public void run(FrameApi frameApi, LuaTable locals, LuaTable globals) {
        mFrameFunc.call(frameApi, locals, globals);
    }
}
