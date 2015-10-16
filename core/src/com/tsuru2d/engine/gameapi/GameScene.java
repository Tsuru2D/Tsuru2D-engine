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

    public static Builder getBuilder(String sceneId) {
        return new Builder(sceneId);
    }

    public static class Builder extends ExposedJavaClass{
        private GameScene mScene;

        public Builder(String sceneId) {
            mScene = new GameScene();
            mScene.mSceneID=sceneId;
        }

        @ExposeToLua
        public void setup(LuaFunction function) {
            mScene.mSetupFunction = function;
        }

        @ExposeToLua
        public void frame(String id, LuaFunction function) {
            mScene.mFrames.put(id, function);
        }

        public GameScene build(){
            return mScene;
        }
    }
}
