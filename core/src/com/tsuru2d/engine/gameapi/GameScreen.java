package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.LuaAssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class GameScreen extends BaseScreen {
    private final Globals mGameState;
    private final LuaTable mGlobalsTable;
    private GameScene mScene;
    private LuaTable mLocals;
    private FrameApi mFrameApi;
    private LuaFunction mClickCallback;
    private Array<GameActor> mActors;
    private boolean mSkipAnimations;
    private Group mGameGroup;

    public GameScreen(EngineMain game, LuaTable screenScript, LuaTable globalsTable) {
        super(game, screenScript);
        mGameState = new Globals();
        mGlobalsTable = globalsTable;
        mActors = new Array<GameActor>();
        mSkipAnimations = false;
        mFrameApi = new FrameApi(this, mGameState, screenScript);
        createActorGraph();
    }

    private void createActorGraph() {
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        Stack stack = new Stack();
        stack.setFillParent(true);

        Group gameGroup = new Group();
        gameGroup.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (mClickCallback != null) {
                    mClickCallback.call();
                }
            }
        });
        stack.addActor(gameGroup);

        Table uiTable = new Table();
        uiTable.setFillParent(true);
        stack.addActor(uiTable);

        rootTable.addActor(stack);

        mGameGroup = gameGroup;
        mRootTable = rootTable;
        mUITable = uiTable;
        mStage.addActor(rootTable);
    }

    public LuaTable getGlobalsTable() {
        return mGlobalsTable;
    }

    public GameScene getScene() {
        return mScene;
    }

    public void setScene(AssetID sceneID, String frameID) {
        for (GameActor actor : mActors) {
            Actor gdxActor = actor.getActor();
            gdxActor.clearActions();
            gdxActor.remove();
            actor.dispose();
        }

        mActors.clear();
        mLocals = new LuaTable();
        GameScene scene = mGame.getAssetLoader().getScene(sceneID);
        mScene = scene;
        scene.runSetup(mFrameApi, mLocals, mGlobalsTable);
        GameFrame frame = scene.getFrame();
        if (frameID != null) {
            mSkipAnimations = true;
            String currentFrameID = scene.getFrameID();
            while (!currentFrameID.equals(frameID)) {
                runFrame(frame);
                frame = mScene.nextFrame();
                currentFrameID = frame.getFrameID();
            }
            mSkipAnimations = false;
        }
        runFrame(frame);
    }

    public GameActor createActor(LuaAssetID id, LuaTable params) {
        LuaTable actorScript = mGame.getAssetLoader().getObject(id.userdata());
        GameActor actor = new GameActor(this, actorScript);
        mGameGroup.addActor(actor.getActor());
        actor.transform(params, true);
        mActors.add(actor);
        return actor;
    }

    private void runFrame(GameFrame frame) {
        frame.run(mFrameApi, mLocals, mGlobalsTable);
    }

    private void nextScene() {
        AssetID nextSceneID = mScene.getNextSceneID();
        setScene(nextSceneID, null);
    }

    @ExposeToLua
    public void setClickListener(LuaFunction callback) {
        mClickCallback = callback;
    }

    @ExposeToLua
    public void gotoFrame(String frameID) {
        GameFrame frame = mScene.gotoFrame(frameID);
        runFrame(frame);
    }

    @ExposeToLua
    public boolean nextFrame() {
        GameFrame frame = mScene.nextFrame();
        if (frame == null) {
            nextScene();
            return false;
        }
        runFrame(frame);
        return true;
    }

    @ExposeToLua
    public void transform(GameActor actor, LuaTable params) {
        actor.transform(params, mSkipAnimations);
    }

    @Override
    public void dispose() {
        for (GameActor actor : mActors) {
            actor.dispose();
        }
        super.dispose();
    }
}
