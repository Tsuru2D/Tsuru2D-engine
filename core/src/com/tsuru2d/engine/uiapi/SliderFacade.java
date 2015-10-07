package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class SliderFacade implements UIWrapper<Slider> {
    private final BaseScreen mScreen;
    private final LuaTable mLuaTable;
    private final Slider mSlider;
    private ManagedAsset<String> mText;

    public SliderFacade(BaseScreen screen, LuaTable data) {
        mLuaTable = data;
        mScreen = screen;
        mSlider = new Slider(0.0f, 1.0f, 0.01f, false, new Skin());
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
    public Slider getActor() {
        return mSlider;
    }

    @Override
    public void dispose() {
    }

}
