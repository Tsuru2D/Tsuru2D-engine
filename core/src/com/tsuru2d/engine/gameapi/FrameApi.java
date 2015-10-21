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
        // TODO
        return new GameActor(null);
    }

    @ExposeToLua
    public InstantGameAction alpha(float alpha) {
        // TODO
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction delay(float secs, LuaFunction callback) {
        // TODO
        if (callback != null) {
            callback.call();
        }
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction interactive(LuaValue params) {
        // TODO
        mUIEnvironment.invokemethod("onInteractive", params);
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public InstantGameAction background(LuaAssetID imageID) {
        mUIEnvironment.invokemethod("onBackground", imageID);
        return InstantGameAction.EMPTY;
    }

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
    public GameAction transform(GameActor obj, LuaTable args) {
        mUIEnvironment.invokemethod("onTransform", LuaValue.varargsOf(new LuaUserdata(obj), args));
        return InstantGameAction.EMPTY;
    }

    @ExposeToLua
    public GameAction camera(LuaValue params) {
        mUIEnvironment.invokemethod("onCamera", params);
        return InstantGameAction.EMPTY;
    }
}