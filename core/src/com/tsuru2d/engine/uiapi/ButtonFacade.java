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
    private LuaFunction mClickedCallback;
    private ManagedAsset<Texture> mUp, mDown, mHover;

    public ButtonFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
    }

    @Override
    protected Button createActor(Button.ButtonStyle style) {
        return new Button(style);
    }

    @Override
    protected void initializeActor(Button actor) {
        actor.addListener(new ClickedHandler());
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

        style.up = toDrawable(mUp);
        style.down = toDrawable(mDown);
        style.over = toDrawable(mHover);
    }

    @ExposeToLua
    public void setOnClickedListener(LuaFunction callback) {
        mClickedCallback = callback;
    }

    @Override
    public void dispose() {
        mUp = freeAsset(mUp);
        mDown = freeAsset(mDown);
        mHover = freeAsset(mHover);
        super.dispose();
    }

    private class ClickedHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mClickedCallback != null) {
                mClickedCallback.call(ButtonFacade.this);
            }
        }
    }
}
