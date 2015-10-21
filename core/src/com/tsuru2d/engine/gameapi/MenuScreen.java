package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.io.GameSaveData;
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
    public MenuScreen(EngineMain game, LuaTable screenScript) {
        super(game, screenScript);
    }

    // TODO: better naming

    @ExposeToLua
    public void pushGameScreenNew(AssetID id, LuaValue params) {
        mGame.pushGameScreen(id, params);
    }

    @ExposeToLua
    public GameSaveData getSaveGame() {
        return new GameSaveData(); // todo
    }

    @ExposeToLua
    public void pushGameScreenResume(GameSaveData saveData, LuaValue params) {
        mGame.pushGameScreen(saveData, params);
    }
}
