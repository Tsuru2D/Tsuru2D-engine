package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class SliderFacade extends UIWrapper<Slider> {
    private Slider.SliderStyle mSliderStyle;
    private final Slider mSlider;

    public SliderFacade(BaseScreen screen, LuaTable data) {
        super(screen, data);
        mSliderStyle = new Slider.SliderStyle();
        mSlider = new Slider(0.0f, 1.0f, 0.01f, false, mSliderStyle);
    }

    @ExposeToLua
    public void setRange(float min, float max) {
        mSlider.setRange(min, max);
    }

    @ExposeToLua
    public void setStepSize(float step) {
        mSlider.setStepSize(step);
    }

    @ExposeToLua
    public float getValue() {
        return mSlider.getValue();
    }

    @Override
    @ExposeToLua
    public void setPosition(float x, float y) {
        mSlider.setPosition(x, y);
    }

    @Override
    public Slider getActor() {
        return mSlider;
    }

    @Override
    public void dispose() {
    }

}
