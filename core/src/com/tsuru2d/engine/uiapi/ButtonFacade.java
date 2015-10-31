package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class ButtonFacade extends ActorFacade<Button> {
    private LuaFunction mClickedCallback;
    private ManagedAsset<Texture> mUp, mDown, mHover;

    public ButtonFacade(BaseScreen screen) {
        super(screen);
        Button button = createActor();
        setActor(button);
        button.addListener(new OnClickHandler());
    }

    protected Button createActor() {
        return new Button(createStyle());
    }

    protected Button.ButtonStyle createStyle() {
        return new Button.ButtonStyle();
    }

    protected void populateStyle(Button.ButtonStyle style, LuaTable styleTable) {
        mUp = swapStyleImage(styleTable, "up", mUp);
        mDown = swapStyleImage(styleTable, "down", mUp);
        mHover = swapStyleImage(styleTable, "hover", mUp);

        style.up = toDrawable(mUp);
        style.down = toDrawable(mDown);
        style.over = toDrawable(mHover);
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        Button.ButtonStyle newStyle = createStyle();
        populateStyle(newStyle, styleTable);
        getActor().setStyle(newStyle);
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
    }

    private class OnClickHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mClickedCallback != null) {
                mClickedCallback.call(ButtonFacade.this);
            }
        }
    }
}
