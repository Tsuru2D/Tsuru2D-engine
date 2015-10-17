package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.LuaMapIterator;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.Iterator;

public class GameScreen extends BaseScreen {
    private final Globals mGameState;
    private GameScene mScene;
    private LuaTable mLocals;
    private final LuaTable mGlobalsTable;
    private FrameApi mFrameApi;

    public GameScreen(EngineMain game, LuaTable screenScript, LuaTable globalsTable) {
        super(game, screenScript);
        mGameState = new Globals();
        mGlobalsTable = globalsTable;
        mFrameApi = new FrameApi(mGameState, screenScript);
    }

    public void setScene(AssetID sceneID, String frameID) {
        if (mLocals != null) {
            Iterator<Varargs> it = new LuaMapIterator(mLocals);
            while (it.hasNext()) {
                Varargs localKvp = it.next();
                LuaValue value = localKvp.arg(2);
                if (value instanceof GameActor) {
                    // TODO: dispose old actor
                }
            }
        }
        GameScene scene = mGame.getAssetLoader().getScene(sceneID);
        mScene = scene;
        mLocals = new LuaTable();
        scene.runSetup(mFrameApi, mLocals, mGlobalsTable);
        GameFrame frame;
        if (frameID != null) {
            frame = scene.gotoFrame(frameID);
        } else {
            frame = scene.getFrame();
        }
        frame.run(mFrameApi, mLocals, mGlobalsTable);
    }

    private void nextScene() {
        AssetID nextSceneID = mScene.getNextSceneID();
        setScene(nextSceneID, null);
    }

    @ExposeToLua
    public void gotoFrame(String frameID) {
        GameFrame frame = mScene.gotoFrame(frameID);
        frame.run(mFrameApi, mLocals, mGlobalsTable);
    }

    @ExposeToLua
    public boolean nextFrame() {
        GameFrame frame = mScene.nextFrame();
        if (frame == null) {
            nextScene();
            return false;
        }
        return true;
    }
}
