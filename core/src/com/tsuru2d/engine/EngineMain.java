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
import com.tsuru2d.engine.loader.*;
import com.tsuru2d.engine.model.GameMetadataInfo;
import org.luaj.vm2.LuaTable;

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
        mViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mSpriteBatch = new SpriteBatch();

        mTitle = mAssetLoader.getText(getMetadata().mTitle);
        mTitle.addObserver(this);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            MetadataLoader.Resolution resolution = getMetadata().mResolution;
            Gdx.graphics.setDisplayMode(resolution.getWidth(), resolution.getHeight(), false);
        }

        mScreens = new ArrayDeque<BaseScreen>();
        pushScreen(mAssetLoader.getMetadata().mMainScreen);
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
        mTitle.removeObserver(this);
        mAssetLoader.freeAsset(mTitle);
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

    public void pushScreen(AssetID screenID) {
        LuaTable screenScript = mAssetLoader.getScreen(screenID);
        BaseScreen screen;
        if (screenID.equals(mAssetLoader.getMetadata().mGameScreen)) {
            screen = new GameScreen(this);
        } else {
            screen = new MenuScreen(this, screenScript);
        }

        if (mScreen != null) {
            mScreen.hide();
        }

        mScreens.push(screen);
        mScreen = screen;
        screen.show();
        screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void setScreen(AssetID screenID) {
        popScreenHelper();
        pushScreen(screenID);
    }

    private BaseScreen popScreenHelper() {
        BaseScreen screen = mScreens.pop();
        screen.hide();
        return mScreens.peek();
    }

    public void popScreen() {
        BaseScreen screen = popScreenHelper();
        if (screen == null) {
            throw new GdxRuntimeException("Cannot pop the root screen");
        }
        mScreen = screen;
        screen.show();
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
