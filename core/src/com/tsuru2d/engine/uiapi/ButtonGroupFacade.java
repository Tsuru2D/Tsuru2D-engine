package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

import java.util.HashMap;

public class ButtonGroupFacade<T extends ButtonSuper>{
    private HashMap<Button, T> mMap;
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
    public void add(T... buttons) {
        if(buttons == null) throw new IllegalArgumentException("button cannot be null.");
        for(T button : buttons) {
            add(button);
        }
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
