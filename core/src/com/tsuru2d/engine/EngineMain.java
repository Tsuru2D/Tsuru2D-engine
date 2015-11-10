package com.tsuru2d.engine;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.gameapi.GameScene;
import com.tsuru2d.engine.gameapi.GameScreen;
import com.tsuru2d.engine.gameapi.MenuScreen;
import com.tsuru2d.engine.io.GameSaveData;
import com.tsuru2d.engine.io.NetManager;
import com.tsuru2d.engine.io.NetManagerImpl;
import com.tsuru2d.engine.loader.*;
import com.tsuru2d.engine.model.GameMetadataInfo;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.ArrayDeque;

/**
 * Similar to a {@link Game}, but allows custom transitions between
 * screens. This is the entry point for the engine code.
 */
public class EngineMain implements ApplicationListener, AssetObserver<String> {
    private PlatformApi mPlatformApi;
    private AssetLoader mAssetLoader;
    private Viewport mViewport;
    private SpriteBatch mSpriteBatch;
    private ManagedAsset<String> mTitle;
    private ManagedAsset<Music> mMusic;
    private ArrayDeque<BaseScreen> mScreens;
    private NetManager mNetManager;
    private BaseScreen mCurrentScreen;

    public EngineMain(PlatformApi platformApi) {
        mPlatformApi = platformApi;
    }

    @Override
    public void create() {
        mAssetLoader = new AssetLoader(mPlatformApi.getRawAssetLoader());
        mNetManager = new NetManagerImpl(getMetadata().mPackageName);
        MetadataLoader.Resolution resolution = getMetadata().mResolution;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.graphics.setDisplayMode(resolution.getWidth(), resolution.getHeight(), false);
        }

        mViewport = new FitViewport(resolution.getWidth(), resolution.getHeight());
        mSpriteBatch = new SpriteBatch();
        mScreens = new ArrayDeque<BaseScreen>();
        mTitle = mAssetLoader.getText(getMetadata().mTitle);
        mTitle.addObserver(this);
        pushMenuScreen(getMetadata().mMainScreen, LuaValue.NIL);
    }

    @Override
    public void onAssetUpdated(ManagedAsset<String> asset) {
        if (asset.getAssetID().equals(getMetadata().mTitle)) {
            Gdx.graphics.setTitle(asset.get());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (mCurrentScreen != null) {
            mCurrentScreen.resize(width, height);
        }
    }

    @Override
    public void render() {
        mAssetLoader.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (mCurrentScreen != null) {
            mCurrentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void pause() {
        if (mCurrentScreen != null) {
            mCurrentScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (mCurrentScreen != null) {
            mCurrentScreen.resume();
        }
    }

    @Override
    public void dispose() {
        if (mMusic != null) {
            mMusic.get().stop();
            mAssetLoader.freeAsset(mMusic);
        }
        disposeAllScreens();
        mTitle.removeObserver(this);
        mAssetLoader.freeAsset(mTitle);
        mTitle = null;
        mAssetLoader.dispose();
    }

    public void playMusic(AssetID musicID) {
        if (mMusic != null) {
            mMusic.get().stop();
            mAssetLoader.freeAsset(mMusic);
            mMusic = null;
        }
        if (musicID != null) {
            mMusic = mAssetLoader.getMusic(musicID);
            mMusic.get().play();
        }
    }

    public void setGameScreen(GameSaveData saveData, LuaValue params) {
        LuaTable globals = saveData.mCustomState;
        AssetID sceneID = AssetID.fromString(saveData.mSceneId);
        String frameID = saveData.mFrameId;
        LuaTable screenScript = mAssetLoader.getScreen(getMetadata().mGameScreen);
        GameScreen screen = new GameScreen(this, screenScript, globals);
        setScreen(screen, params);
        screen.setScene(sceneID, frameID);
    }

    public void setGameScreen(AssetID sceneID, LuaValue params) {
        // TODO: lazy hack, rewrite this properly sometime
        GameSaveData saveData = new GameSaveData();
        saveData.mCustomState = new LuaTable();
        saveData.mSceneId = sceneID.toString();
        setGameScreen(saveData, params);
    }

    public void pushMenuScreen(AssetID screenID, LuaValue params) {
        LuaTable screenScript = mAssetLoader.getScreen(screenID);
        MenuScreen screen = new MenuScreen(this, screenScript);
        pushScreen(screen, params);
    }

    public void setMenuScreen(AssetID screenID, LuaValue params) {
        LuaTable screenScript = mAssetLoader.getScreen(screenID);
        MenuScreen screen = new MenuScreen(this, screenScript);
        setScreen(screen, params);
    }

    public void popScreen(LuaValue params) {
        BaseScreen screen = mScreens.pop();
        screen.hide();
        screen.dispose();
        BaseScreen newScreen = mScreens.peek();
        if (newScreen == null) {
            throw new GdxRuntimeException("Cannot pop the root screen");
        }
        mCurrentScreen = newScreen;
        showScreen(newScreen, params);
    }

    private void showScreen(BaseScreen screen, LuaValue params) {
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        screen.show(params);
    }

    private void pushScreen(BaseScreen screen, LuaValue params) {
        mScreens.push(screen);
        mCurrentScreen = screen;
        screen.inititialize();
        showScreen(screen, params);
    }

    private void disposeAllScreens() {
        if (mCurrentScreen != null) {
            mCurrentScreen.hide();
            while (!mScreens.isEmpty()) {
                mScreens.pop().dispose();
            }
        }
    }

    private void setScreen(BaseScreen screen, LuaValue params) {
        disposeAllScreens();
        pushScreen(screen, params);
    }

    public void setLanguage(String languageCode) {
        mAssetLoader.setLanguage(languageCode);
    }

    public SpriteBatch getSpriteBatch() {
        return mSpriteBatch;
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public AssetLoader getAssetLoader() {
        return mAssetLoader;
    }

    public NetManager getNetManager() {
        return mNetManager;
    }

    public GameMetadataInfo getMetadata() {
        return mAssetLoader.getMetadata();
    }

    public GameSaveData buildSaveData() {
        GameSaveData saveData = new GameSaveData();
        saveData.mCreationTime = System.currentTimeMillis() / 1000;
        saveData.mVersion = getMetadata().mVersionCode;
        for (BaseScreen screen : mScreens) {
            if (screen instanceof GameScreen) {
                GameScene currentScene = ((GameScreen)screen).getScene();
                saveData.mSceneId = currentScene.getSceneID().toString();
                saveData.mFrameId = currentScene.getFrameID();
                saveData.mCustomState = ((GameScreen)screen).getGlobalsTable();
                return saveData;
            }
        }
        throw new GdxRuntimeException("No active game screen found");
    }
}
