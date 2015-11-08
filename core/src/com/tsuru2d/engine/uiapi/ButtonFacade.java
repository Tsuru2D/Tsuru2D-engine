package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class ButtonFacade extends ActorFacade<Button, Button.ButtonStyle> {
    private LuaFunction mClickCallback;
    private ManagedAsset<Texture> mUp, mDown, mHover, mChecked, mCheckedHover;

    public ButtonFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
    }

    @Override
    protected Button createActor(Button.ButtonStyle style) {
        return new Button(style);
    }

    @Override
    protected void initializeActor(Button actor) {
        actor.addListener(new ClickHandler());
    }

    @Override
    protected Button.ButtonStyle createStyle() {
        return new Button.ButtonStyle();
    }

    @Override
    protected void populateStyle(Button.ButtonStyle style, LuaTable styleTable) {
        mUp = swapStyleImage(styleTable, "up", mUp);
        mDown = swapStyleImage(styleTable, "down", mDown);
        mHover = swapStyleImage(styleTable, "hover", mHover);
        mChecked=swapStyleImage(styleTable,"checked",mChecked);
        mCheckedHover=swapStyleImage(styleTable,"checkedHover",mCheckedHover);

        style.up = toDrawable(mUp);
        style.down = toDrawable(mDown);
        style.over = toDrawable(mHover);
        style.checked=toDrawable(mChecked);
        style.checkedOver=toDrawable(mCheckedHover);
    }

    @ExposeToLua
    public void setClickListener(LuaFunction callback) {
        mClickCallback = callback;
    }

    @ExposeToLua
    public boolean isEnabled() {
        return !getActor().isDisabled();
    }

    @ExposeToLua
    public void setEnabled(boolean enabled) {
        getActor().setDisabled(!enabled);
    }

    @ExposeToLua
    public void setChecked(boolean checked){
        getActor().setProgrammaticChangeEvents(false);
        getActor().setChecked(checked);
        getActor().setProgrammaticChangeEvents(true);
    }

    @Override
    public void dispose() {
        mUp = freeAsset(mUp);
        mDown = freeAsset(mDown);
        mHover = freeAsset(mHover);
        mChecked=freeAsset(mChecked);
        mCheckedHover=freeAsset(mCheckedHover);
        super.dispose();
    }

    private class ClickHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mClickCallback != null) {
                mClickCallback.call(ButtonFacade.this);
            }
        }
    }
}
