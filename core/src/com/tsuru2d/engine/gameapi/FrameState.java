package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.loader.LuaAssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class FrameState {
    private final Globals mGlobals;
    private final LuaTable mUIEnvironment;

    public FrameState(Globals globals, LuaTable uiEnvironment) {
        mGlobals = globals;
        mUIEnvironment = uiEnvironment;
    }

    @ExposeToLua
    private GameActor create(LuaAssetID id, LuaTable params) {
        return null;
    }

    @ExposeToLua
    private GameAction delay(double secs, LuaFunction function) {
        return null;
    }

    @ExposeToLua
    private InstantGameAction character(LuaAssetID characterID) {
        mUIEnvironment.get("onCharacter").call(characterID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    private InstantGameAction music(LuaAssetID musicID) {
        mUIEnvironment.get("onMusic").call(musicID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    private InstantGameAction sound(LuaAssetID soundID) {
        mUIEnvironment.get("onSound").call(soundID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    private InstantGameAction voice(LuaAssetID voiceID) {
        mUIEnvironment.get("onVoice").call(voiceID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    private InstantGameAction text(LuaAssetID textID) {
        mUIEnvironment.get("onVoice").call(textID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    private InstantGameAction background(LuaAssetID imageID) {
        mUIEnvironment.get("onBackground").call(imageID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    private GameAction transform(GameActor obj, LuaTable args) {
        AsyncGameAction token = new AsyncGameAction(mGlobals, null);
        return null;
    }

    @ExposeToLua
    private GameAction camera(LuaTable params) {
        return null;
    }

    public void update(float dt) {

    }
}
