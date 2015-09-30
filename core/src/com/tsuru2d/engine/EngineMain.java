package com.tsuru2d.engine;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.tsuru2d.engine.loader.*;
import com.tsuru2d.engine.model.MetadataInfo;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Similar to a {@link Game}, but allows custom transitions between
 * screens. This is the entry point for the engine code.
 */
public class EngineMain implements ApplicationListener, AssetObserver<String> {
    private FileHandleResolver mHandleResolver;
    private AssetFinder mAssetFinder;
    private AssetPathResolver mPathResolver;
    private MetadataInfo mMetadata;
    private Globals mLuaContext;
    private BaseScreen mScreen;
    private Viewport mViewport;
    private SpriteBatch mSpriteBatch;
    private AssetLoader mAssetLoader;
    private ManagedAsset<String> mTitle;
    int mInt = 0;

    public EngineMain(FileHandleResolver handleResolver, AssetFinder assetFinder) {
        mHandleResolver = handleResolver;
        mAssetFinder = assetFinder;
    }

    @Override
    public void create() {
        mLuaContext = createLuaContext();
        mMetadata = MetadataLoader.getMetadata(mLuaContext, mHandleResolver);
        mPathResolver = new AssetPathResolver(mAssetFinder, mMetadata.mLocalizationDir, mMetadata.mAssetDirs);
        mAssetLoader = new AssetLoader(this, mHandleResolver, mPathResolver);
        mPathResolver.setLanguage("en");
        mViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mSpriteBatch = new SpriteBatch();

        mTitle = mAssetLoader.getText(mMetadata.mTitle);
        mTitle.addObserver(this);

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {
            setDisplayResolution(mMetadata.mResolution);
        }
    }

    private void setDisplayResolution(String resolution) {
        Pattern pattern = Pattern.compile("(\\d+)x(\\d+)");
        Matcher matcher = pattern.matcher(resolution);
        if (matcher.matches()) {
            int width = Integer.parseInt(matcher.group(1));
            int height = Integer.parseInt(matcher.group(2));
            Gdx.graphics.setDisplayMode(width, height, false);
        } else {
            throw new IllegalArgumentException("Invalid resolution string: " + resolution);
        }
    }

    @Override
    public void onAssetUpdated(ManagedAsset<String> asset) {
        if (asset.getAssetID().equals(mMetadata.mTitle)) {
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
        if (++mInt % 100 == 0) {
            mPathResolver.setLanguage((mInt % 200 == 0) ? "en" : "zh");
        }
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
        mLuaContext = null;
    }

    public Globals getLuaContext() {
        return mLuaContext;
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

    public MetadataInfo getMetadata() {
        return mMetadata;
    }

    private static Globals createLuaContext() {
        // Games should never use the os and io
        // libraries, so let's get rid of those
        Globals globals = new Globals();
        globals.load(new JseBaseLib());
        globals.load(new PackageLib());
        globals.load(new Bit32Lib());
        globals.load(new TableLib());
        globals.load(new StringLib());
        globals.load(new JseMathLib());
        globals.load(new LuaAssetIDLib());
        LoadState.install(globals);
        LuaC.install(globals);
        return globals;
    }
}
