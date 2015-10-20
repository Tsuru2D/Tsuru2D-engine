package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.util.DrawableLoader;
import org.luaj.vm2.LuaTable;

public class SliderFacade extends UIWrapper<Slider> {
    private Slider.SliderStyle mSliderStyle;
    private DrawableLoader mDrawableLoader;
    private final Slider mSlider;

    public SliderFacade(BaseScreen screen, LuaTable data) {
        super(screen);
        mSliderStyle = new Slider.SliderStyle();
        mSlider = new Slider(0.0f, 1.0f, 0.01f, false, mSliderStyle);
    }

    @ExposeToLua
    public void setZipPath(AssetID zipPath) {
        try {
            mDrawableLoader =
                    DrawableLoader.getDrawableLoader(mScreen.getAssetLoader().getText(zipPath).get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @ExposeToLua
    public void setKnodImage(AssetID innerPath) {
        Drawable drawable =
                mDrawableLoader.getDrawable(mScreen.getAssetLoader().getText(innerPath).get());
        mSliderStyle.knob = drawable;
    }

    @ExposeToLua
    public void setBackgroundImage(AssetID innerPath) {
        Drawable drawable =
                mDrawableLoader.getDrawable(mScreen.getAssetLoader().getText(innerPath).get());
        mSliderStyle.background = drawable;
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
