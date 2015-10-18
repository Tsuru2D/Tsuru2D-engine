package com.tsuru2d.engine.gameapi;

import com.tsuru2d.engine.loader.LuaAssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.*;

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
        mUIEnvironment.invokemethod("onCharacter", characterID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction music(LuaAssetID musicID) {
        mUIEnvironment.invokemethod("onMusic", musicID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction sound(LuaAssetID soundID) {
        mUIEnvironment.invokemethod("onSound", soundID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction voice(LuaAssetID voiceID) {
        mUIEnvironment.invokemethod("onVoice", voiceID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction text(LuaAssetID textID) {
        mUIEnvironment.invokemethod("onText", textID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction background(LuaAssetID imageID) {
        mUIEnvironment.invokemethod("onBackground", imageID);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public GameAction transform(GameActor obj, LuaTable args) {
        mUIEnvironment.invokemethod("onTransform", LuaValue.varargsOf(new LuaUserdata(obj), args));
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public GameAction camera(LuaTable params) {
        mUIEnvironment.invokemethod("onCamera", params);
        return InstantGameAction.EMPTY;
    }
}
