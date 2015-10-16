package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaFunction;

import java.util.LinkedHashMap;

public class GameScene {
    private String mSceneID;
    private LuaFunction mSetupFunction;
    private LinkedHashMap<String, LuaFunction> mFrames;

    private GameScene() {
        mFrames = new LinkedHashMap<String, LuaFunction>();
    }

    public static ExposedJavaClass getBuilder() {
        return new ExposedJavaClass(new Builder());
    }

    public static class Builder {
        @ExposeToLua
        public void setup(LuaFunction function) {

        }

        @ExposeToLua
        public void frame(String id, LuaFunction function) {

        }
    }
}
