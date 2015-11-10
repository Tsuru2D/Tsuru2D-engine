package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.LuaAssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class GameScene {
    private AssetID mNextSceneID;
    private AssetID mSceneID;
    private LuaFunction mSetupFunction;
    private Array<GameFrame> mFrames;
    private int mFrameIndex;

    private GameScene() {
        mFrames = new Array<GameFrame>();
        mFrameIndex = 0;
    }

    public AssetID getNextSceneID() {
        return mNextSceneID;
    }

    public AssetID getSceneID() {
        return mSceneID;
    }

    public String getFrameID() {
        return mFrames.get(mFrameIndex).getFrameID();
    }

    public void runSetup(FrameApi frameApi, LuaTable locals, LuaTable globals) {
        mSetupFunction.call(frameApi, locals, globals);
    }

    public GameFrame getFrame() {
        return mFrames.get(mFrameIndex);
    }

    public GameFrame gotoFrame(String frameID) {
        for (int i = 0; i < mFrames.size; ++i) {
            GameFrame frame = mFrames.get(i);
            if (frame.getFrameID().equals(frameID)) {
                mFrameIndex = i;
                return frame;
            }
        }
        throw new GdxRuntimeException("Could not find frame with ID: " + frameID);
    }

    public GameFrame nextFrame() {
        if (mFrameIndex == mFrames.size - 1) {
            return null;
        }
        return mFrames.get(++mFrameIndex);
    }

    public static GameScene loadFunc(AssetID sceneID, LuaFunction sceneFunc) {
        Builder sceneBuilder = new Builder();
        LuaAssetID nextSceneID = (LuaAssetID)sceneFunc.call(sceneBuilder);
        GameScene scene = sceneBuilder.build();
        scene.mSceneID = sceneID;
        scene.mNextSceneID = nextSceneID.userdata();
        return scene;
    }

    private static class Builder extends ExposedJavaClass {
        private GameScene mScene;

        public Builder() {
            mScene = new GameScene();
        }

        @ExposeToLua
        public void setup(LuaFunction function) {
            mScene.mSetupFunction = function;
        }

        @ExposeToLua
        public void frame(String id, LuaFunction function) {
                mScene.mFrames.add(new GameFrame(id, function));
            }

        public GameScene build() {
            return mScene;
        }
    }
}
