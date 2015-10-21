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
import com.tsuru2d.engine.gameapi.GameScreen;
import com.tsuru2d.engine.gameapi.MenuScreen;
import com.tsuru2d.engine.io.GameSaveData;
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
    private BaseScreen mScreen;
    private AssetLoader mAssetLoader;
    private Viewport mViewport;
    private SpriteBatch mSpriteBatch;
    private ManagedAsset<String> mTitle;
    private ManagedAsset<Music> mMusic;
    private ArrayDeque<BaseScreen> mScreens;

    public EngineMain(PlatformApi platformApi) {
        mPlatformApi = platformApi;
    }

    @Override
    public void create() {
        mAssetLoader = new AssetLoader(mPlatformApi.getRawAssetLoader());
        MetadataLoader.Resolution resolution = getMetadata().mResolution;
        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            Gdx.graphics.setDisplayMode(resolution.getWidth(), resolution.getHeight(), false);
        }

        mViewport = new FitViewport(resolution.getWidth(), resolution.getHeight());
        mSpriteBatch = new SpriteBatch();
        mScreens = new ArrayDeque<BaseScreen>();
        mTitle = mAssetLoader.getText(getMetadata().mTitle);
        mTitle.addObserver(this);
        pushScreen(mAssetLoader.getMetadata().mMainScreen, LuaValue.NIL);
    }

    @Override
    public void onAssetUpdated(ManagedAsset<String> asset) {
        if (asset.getAssetID().equals(getMetadata().mTitle)) {
            Gdx.graphics.setTitle(asset.get());
        }
    }

    @Override
    public void resize(int width, int height) {
        if (mScreen != null) {
            mScreen.resize(width, height);
        }
    }

    @Override
    public void render() {
        mAssetLoader.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (mScreen != null) {
            mScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void pause() {
        if (mScreen != null) {
            mScreen.pause();
        }
    }

    @Override
    public void resume() {
        if (mScreen != null) {
            mScreen.resume();
        }
    }

    @Override
    public void dispose() {
        if (mMusic != null) {
            mMusic.get().stop();
            mAssetLoader.freeAsset(mMusic);
        }
        mTitle.removeObserver(this);
        mAssetLoader.freeAsset(mTitle);
        mAssetLoader.dispose();
    }

    public void playMusic(AssetID musicID) {
        if (mMusic != null) {
            mMusic.get().stop();
            mAssetLoader.freeAsset(mMusic);
        }
        if (musicID != null) {
            mMusic = mAssetLoader.getMusic(musicID);
            mMusic.get().play();
        }
    }

    private void initScreen(BaseScreen screen, LuaValue params) {
        mScreen = screen;
        screen.show(params);
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void pushGameScreen(GameSaveData saveData, LuaValue params) {
        LuaTable globals = saveData.mCustomState;
        AssetID sceneID = AssetID.fromString(saveData.mSceneId);
        String frameID = saveData.mFrameId;
        LuaTable screenScript = mAssetLoader.getScreen(getMetadata().mGameScreen);
        GameScreen screen = new GameScreen(this, screenScript, globals);
        screen.setScene(sceneID, frameID);
        mScreens.push(screen);
        initScreen(screen, params);
    }

    public void pushGameScreen(AssetID sceneID, LuaValue params) {
        // TODO: lazy hack, rewrite this properly sometime
        GameSaveData saveData = new GameSaveData();
        saveData.mCustomState = new LuaTable();
        saveData.mSceneId = sceneID.toString();
        pushGameScreen(saveData, params);
    }

    public void pushScreen(AssetID screenID, LuaValue params) {
        LuaTable screenScript = mAssetLoader.getScreen(screenID);
        BaseScreen screen = new MenuScreen(this, screenScript);
        mScreens.push(screen);
        initScreen(screen, params);
    }

    public void setScreen(AssetID screenID, LuaValue params) {
        popScreenHelper();
        pushScreen(screenID, params);
    }

    private BaseScreen popScreenHelper() {
        BaseScreen screen = mScreens.pop();
        screen.hide();
        screen.dispose();
        return mScreens.peek();
    }

    public void popScreen(LuaValue params) {
        BaseScreen screen = popScreenHelper();
        if (screen == null) {
            throw new GdxRuntimeException("Cannot pop the root screen");
        }
        initScreen(screen, params);
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

    public GameMetadataInfo getMetadata() {
        return mAssetLoader.getMetadata();
    }
}
