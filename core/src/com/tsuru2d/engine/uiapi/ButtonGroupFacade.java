package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;

public class ButtonGroupFacade{
    private final ButtonGroup mButtonGroup;

    public ButtonGroupFacade(BaseScreen screen) {
        mButtonGroup = new ButtonGroup();
        mButtonGroup.setMaxCheckCount(1);
        mButtonGroup.setMinCheckCount(0);
    }

    @ExposeToLua
    public void add(CheckBoxFacade checkBox) {
        if(checkBox == null) throw new IllegalArgumentException("button cannot be null.");
        mButtonGroup.add(checkBox.getActor());
    }

    @ExposeToLua
    public void remove(CheckBoxFacade checkBox) {
        if(checkBox == null) throw new IllegalArgumentException("button cannot be null.");
        mButtonGroup.remove(checkBox.getActor());
    }

    @ExposeToLua
    public void setMinCheckCount(int min) {
        mButtonGroup.setMinCheckCount(min);
    }

    @ExposeToLua
    public void setMaxCheckCount(int max) {
        mButtonGroup.setMaxCheckCount(max);
    }

}
