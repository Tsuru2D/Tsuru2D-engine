package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.tsuru2d.engine.lua.ExposeToLua;

/*
 * The Radio Buttons container. Controls how many total selection can make by the buttons in
 * the container.
 */

public class ButtonGroupFacade<T extends ButtonSuper>{
    private final ButtonGroup mButtonGroup;


    public ButtonGroupFacade() {
        mButtonGroup = new ButtonGroup();
    }

    @ExposeToLua
    public void add(T button) {
        if(button == null) throw new IllegalArgumentException("button cannot be null.");
        mButtonGroup.add(button.getActor());
    }

    @ExposeToLua
    public void remove(T button) {
        if(button == null) throw new IllegalArgumentException("button cannot be null.");
        mButtonGroup.remove(button.getActor());
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
