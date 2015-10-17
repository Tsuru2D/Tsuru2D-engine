package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.loader.LuaAssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaUserdata;

public class FrameApi extends ExposedJavaClass {
    private final Globals mGlobals;
    private final LuaTable mUIEnvironment;

    public FrameApi(Globals globals, LuaTable uiEnvironment) {
        mGlobals = globals;
        mUIEnvironment = uiEnvironment;
    }

    @ExposeToLua
    public GameActor create(LuaAssetID id, LuaTable params) {
        return null; //return new GameActor(id, params);
    }

    /*@ExposeToLua
    public GameAction delay(double secs, LuaFunction function) {
        return null;
    }*/

    @ExposeToLua
    public InstantGameAction character(LuaAssetID characterID) {
        mUIEnvironment.get("onCharacter").call(characterID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction music(LuaAssetID musicID) {
        mUIEnvironment.get("onMusic").call(musicID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction sound(LuaAssetID soundID) {
        mUIEnvironment.get("onSound").call(soundID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction voice(LuaAssetID voiceID) {
        mUIEnvironment.get("onVoice").call(voiceID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction text(LuaAssetID textID) {
        mUIEnvironment.get("onVoice").call(textID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction background(LuaAssetID imageID) {
        mUIEnvironment.get("onBackground").call(imageID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public GameAction transform(GameActor obj, LuaTable args) {
        mUIEnvironment.get("onTransform").call(new LuaUserdata(obj), args);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public GameAction camera(LuaTable params) {
        mUIEnvironment.get("onCamera").call(params);
        return InstantGameAction.EMPTY;
    }
}
