package com.tsuru2d.engine;

import org.luaj.vm2.Globals;

public class GameScreen extends BaseScreen {
    private final Globals mGameState;

    public GameScreen(EngineMain game) {
        super(game);
        mGameState = new Globals();
    }
}
