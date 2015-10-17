package com.tsuru2d.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

/**
 * The Java class for all menu screens. Each screen object
 * encapsulates one {@link Stage} object and exposes its
 * features to Lua.
 */
public class MenuScreen extends BaseScreen {
    private LuaTable mScreenScript;

    public MenuScreen(EngineMain game, LuaTable screenScript) {
        super(game);
        mScreenScript = screenScript;
    }

    @Override
    public void show() {
        super.show();
        mScreenScript.invokemethod("onCreate", this);
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
