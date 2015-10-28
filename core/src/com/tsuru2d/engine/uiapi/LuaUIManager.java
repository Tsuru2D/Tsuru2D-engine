package com.tsuru2d.engine.uiapi;

import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public class LuaUIManager extends ExposedJavaClass {
    private final BaseScreen mScreen;

    public LuaUIManager(BaseScreen screen) {
        mScreen = screen;
    }

    @ExposeToLua
    public TableFacade newTable() {
        return new TableFacade(mScreen);
    }

    @ExposeToLua
    public TextButtonFacade newTextButton() {
        return new TextButtonFacade(mScreen);
    }

    @ExposeToLua
    public ButtonFacade newButton() {
        return new ButtonFacade(mScreen);
    }

    @ExposeToLua
    public LabelFacade newLabel() {
        return new LabelFacade(mScreen);
    }
}
