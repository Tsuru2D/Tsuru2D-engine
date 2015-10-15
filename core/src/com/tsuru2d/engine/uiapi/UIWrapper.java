package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public abstract class UIWrapper<T extends Actor> implements Disposable {

    protected LuaTable mLuaTable;
    protected final BaseScreen mScreen;

    abstract T getActor();

    @ExposeToLua
    public void setPosition(float x, float y) {
        getActor().setPosition(x, y);
    }

    public UIWrapper(BaseScreen screen, LuaTable luaTable) {
        mScreen = screen;
        mLuaTable = luaTable;
    }

}
