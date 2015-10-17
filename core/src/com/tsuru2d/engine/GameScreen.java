package com.tsuru2d.engine;

import com.tsuru2d.engine.loader.AssetID;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;

public class GameScreen extends BaseScreen {
    private final Globals mGameState;

    public GameScreen(EngineMain game, LuaTable screenScript, AssetID initialFrame) {
        super(game, screenScript);
        mGameState = new Globals();
    }
}
