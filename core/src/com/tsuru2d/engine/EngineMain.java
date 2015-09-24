package com.tsuru2d.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LoadState;
import org.luaj.vm2.compiler.LuaC;
import org.luaj.vm2.lib.Bit32Lib;
import org.luaj.vm2.lib.PackageLib;
import org.luaj.vm2.lib.StringLib;
import org.luaj.vm2.lib.TableLib;
import org.luaj.vm2.lib.jse.JseBaseLib;
import org.luaj.vm2.lib.jse.JseMathLib;

/**
 * Similar to a {@link Game}, but allows custom transitions between
 * screens. This is the entry point for the engine code.
 */
public class EngineMain implements ApplicationListener {
    private Globals mLuaContext;
    private BaseScreen mScreen;
    private Viewport mViewport;
    private SpriteBatch mSpriteBatch;

    public EngineMain() {
        // TODO: Somehow get an asset loader thing here
    }

    @Override
    public void create() {
        mLuaContext = createLuaContext();
        mViewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mSpriteBatch = new SpriteBatch();
    }

    @Override
    public void resize(int width, int height) {
        if (mScreen != null) {
            mScreen.resize(width, height);
        }
    }

    @Override
    public void render() {
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
        mLuaContext = null;
    }

    public SpriteBatch getSpriteBatch() {
        return mSpriteBatch;
    }

    public Viewport getViewport() {
        return mViewport;
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
        LoadState.install(globals);
        LuaC.install(globals);
        return globals;
    }
}
