package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.BaseScreen;
import org.luaj.vm2.LuaTable;

public abstract class UIWrapper<T extends Actor> implements Disposable {

    protected static BaseScreen mScreen;
    protected LuaTable mLuaTable;

    abstract T getActor();

    public static void setScreen(BaseScreen screen) {
        mScreen = screen;
    }

}
