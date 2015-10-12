package com.tsuru2d.engine;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tsuru2d.engine.loader.AssetLoader;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.loader.MetadataLoader;
import com.tsuru2d.engine.model.GameMetadataInfo;

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

    public SpriteBatch getSpriteBatch() {
        return mSpriteBatch;
    }

    public Viewport getViewport() {
        return mViewport;
    }

    public AssetLoader getAssetLoader() {
        return mAssetLoader;
    }

    public void setScreen(BaseScreen screen, Action action) {
        if (mScreen != null) {
            mScreen.hide();
        }

        mScreen = screen;
        if (mScreen != null) {
            mScreen.show();
            mScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    public GameMetadataInfo getMetadata() {
        return mAssetLoader.getMetadata();
    }
}
