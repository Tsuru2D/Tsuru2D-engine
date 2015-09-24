package com.tsuru2d.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * The Java class for all menu screens. Each screen object
 * encapsulates one {@link Stage} object and exposes its
 * features to Lua.
 */
public class MenuScreen extends BaseScreen {
    public MenuScreen(EngineMain game) {
        super(game);
    }

    @ExposeToLua
    private void setScreen(AssetID id, LuaTable data) {
        MenuScreen screen = null; // TODO: load by ID
        mGame.setScreen(screen, null);
    }

    @ExposeToLua
    private void pushScreen(AssetID id, LuaTable data) {

    }

    @ExposeToLua
    private void popScreen() {

    }

    @Override
    public void show() {
        LuaValue function = mLuaEnvironment.get("onShow");
        if (!function.isnil()) {
            LuaTable data = null; // getData();
            if (data != null) {
                function.checkfunction().call(data);
            } else {
                function.checkfunction().call();
            }
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mStage.act(delta);
        mStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mStage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        LuaValue function = mLuaEnvironment.get("onHide");
        if (!function.isnil()) {
            function.checkfunction().call();
        }
    }

    @Override
    public void dispose() {
        mStage.dispose();
    }
}
