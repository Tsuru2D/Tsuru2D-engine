package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class SliderFacade extends ActorFacade<Slider>{
    private ManagedAsset<Texture> mBackground, mDisabledBackground,
                                    mKnob, mDisabledKnob;

    public SliderFacade(BaseScreen screen) {
        super(screen);
        Slider slider = new Slider(0.0f, 1.0f, 0.01f, false, getStyle());
        setActor(slider);
    }

    public Slider.SliderStyle getStyle() {
        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = mBackground == null ? null : new TextureRegionDrawable(new TextureRegion(mBackground.get()));
        style.disabledBackground =
                mDisabledBackground == null ? null : new TextureRegionDrawable(new TextureRegion(mDisabledBackground.get()));
        style.disabledKnob =
                mDisabledKnob == null ? null : new TextureRegionDrawable(new TextureRegion(mDisabledKnob.get()));
        style.knob =
                mKnob == null ? null : new TextureRegionDrawable(new TextureRegion(mKnob.get()));
        return style;
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        dispose();
        mBackground = getStyleAsset(styleTable, "background");
        mDisabledBackground = getStyleAsset(styleTable, "disabledbackground");
        mKnob = getStyleAsset(styleTable, "knob");
        mDisabledBackground = getStyleAsset(styleTable, "disabledknob");
        Slider.SliderStyle sliderStyle = getStyle();
        getActor().setStyle(sliderStyle);
    }

    @ExposeToLua
    public void setRange(float min, float max) {
        getActor().setRange(min, max);
    }

    @ExposeToLua
    public void setStepSize(float step) {
        getActor().setStepSize(step);
    }

    @ExposeToLua
    public float getValue() {
        return getActor().getValue();
    }

    @ExposeToLua
    public void setDisabled(boolean disabled) {
        getActor().setDisabled(disabled);
    }

    @ExposeToLua
    public boolean isDisabled() {
        return getActor().isDisabled();
    }

    @Override
    public void dispose() {
        if(mBackground != null) {mScreen.getAssetLoader().freeAsset(mBackground); mBackground = null;}
        if(mDisabledBackground != null) {
            mScreen.getAssetLoader().freeAsset(mDisabledBackground); mDisabledBackground = null;
        }
        if(mKnob != null) {mScreen.getAssetLoader().freeAsset(mKnob); mKnob = null;}
        if(mDisabledKnob != null) {
            mScreen.getAssetLoader().freeAsset(mDisabledKnob); mDisabledKnob = null;
        }
    }
}
