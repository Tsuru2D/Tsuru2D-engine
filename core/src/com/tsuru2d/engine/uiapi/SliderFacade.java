package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class SliderFacade extends ActorFacade<Slider, Slider.SliderStyle> {
    private ManagedAsset<Texture> mBackground, mDisabledBackground,
                                  mKnob, mDisabledKnob;
    private LuaFunction mValueChangedCallback;

    public SliderFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
    }

    @Override
    protected Slider createActor(Slider.SliderStyle style) {
        Slider slider = new Slider(0.0f, 1.0f, 0.01f, false, style);
        slider.addListener(new ValueChangedHandler());
        return slider;
    }

    @Override
    protected Slider.SliderStyle createStyle() {
        return new Slider.SliderStyle();
    }

    @Override
    protected void populateStyle(Slider.SliderStyle style, LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, "background", mBackground);
        mDisabledBackground = swapStyleImage(styleTable, "disabledBackground", mDisabledBackground);
        mKnob = swapStyleImage(styleTable, "knob", mKnob);
        mDisabledKnob = swapStyleImage(styleTable, "disabledKnob", mDisabledKnob);

        style.background = toDrawable(mBackground);
        style.disabledBackground = toDrawable(mDisabledBackground);
        style.knob = toDrawable(mKnob);
        style.disabledKnob = toDrawable(mDisabledKnob);
    }

    @ExposeToLua
    public void setOnValueChangedListener(LuaFunction callback) {
        mValueChangedCallback = callback;
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
    public void setEnabled(boolean enabled) {
        getActor().setDisabled(!enabled);
    }

    @ExposeToLua
    public boolean isEnabled() {
        return !getActor().isDisabled();
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        mDisabledBackground = freeAsset(mDisabledBackground);
        mKnob = freeAsset(mKnob);
        mDisabledKnob = freeAsset(mDisabledKnob);
        super.dispose();
    }

    private class ValueChangedHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mValueChangedCallback != null) {
                mValueChangedCallback.call(SliderFacade.this);
            }
        }
    }
}
