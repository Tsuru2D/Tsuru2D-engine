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
import org.luaj.vm2.LuaValue;

public class SliderFacade extends ActorFacade<Slider, Slider.SliderStyle> {
    private ManagedAsset<Texture> mTrack, mTrackDisabled,
        mThumb, mThumbDisabled;
    private LuaFunction mValueChangedCallback;

    public SliderFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
    }

    @Override
    protected Slider createActor(Slider.SliderStyle style) {
        return new Slider(0.0f, 1.0f, 0.01f, false, style);
    }

    @Override
    protected void initializeActor(Slider actor) {
        actor.addListener(new ValueChangedHandler());
    }

    @Override
    protected Slider.SliderStyle createStyle() {
        return new Slider.SliderStyle();
    }

    @Override
    protected void populateStyle(Slider.SliderStyle style, LuaTable styleTable) {
        mTrack = swapStyleImage(styleTable, TRACK, mTrack);
        mTrackDisabled = swapStyleImage(styleTable, TRACK_DISABLED, mTrackDisabled);
        mThumb = swapStyleImage(styleTable, THUMB, mThumb);
        mThumbDisabled = swapStyleImage(styleTable, THUMB_DISABLED, mThumbDisabled);

        style.background = toDrawable(mTrack);
        style.disabledBackground = toDrawable(mTrackDisabled);
        style.knob = toDrawable(mThumb);
        style.disabledKnob = toDrawable(mThumbDisabled);
    }

    @ExposeToLua
    public void setValueChangedListener(LuaFunction callback) {
        // Note: there is an issue with the callback being run twice when
        // the slider bar is clicked directly. This is a limitation with
        // libGDX, and we can't do much about it. Generally speaking though,
        // this is not a big problem, since the callback will be run many
        // times when dragging the knob anyways.
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
    public void setValue(float value) {
        getActor().setValue(value);
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
        mTrack = freeAsset(mTrack);
        mTrackDisabled = freeAsset(mTrackDisabled);
        mThumb = freeAsset(mThumb);
        mThumbDisabled = freeAsset(mThumbDisabled);
        super.dispose();
    }

    private class ValueChangedHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mValueChangedCallback != null) {
                mValueChangedCallback.call(SliderFacade.this, LuaValue.valueOf(getValue()));
            }
        }
    }
}
