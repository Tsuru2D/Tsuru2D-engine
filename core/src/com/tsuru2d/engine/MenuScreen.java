package com.tsuru2d.engine;

import com.badlogic.gdx.scenes.scene2d.Stage;
import org.luaj.vm2.LuaTable;

/**
 * The Java class for all menu screens. Each screen object
 * encapsulates one {@link Stage} object and exposes its
 * features to Lua.
 */
public class MenuScreen extends BaseScreen {
    public MenuScreen(EngineMain game, LuaTable screenScript) {
        super(game, screenScript);
    }
}
