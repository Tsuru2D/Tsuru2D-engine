package com.tsuru2d.engine.lua;

import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.uiapi.ButtonFacade;
import com.tsuru2d.engine.uiapi.LabelFacade;
import com.tsuru2d.engine.uiapi.TableFacade;
import com.tsuru2d.engine.uiapi.TextButtonFacade;

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
