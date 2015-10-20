package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.util.DrawableLoader;
import org.luaj.vm2.LuaFunction;

public class ButtonFacade extends UIWrapper<Button> {
    private ManagedAsset<String> mText;
    private TextObserver mObserver ;
    private ChangeHandler mClickHandler;
    private LuaFunction mCallBack;
    private TextButton.TextButtonStyle mTextButtonStyle;
    private BitmapFont mFont;
    private final TextButton mButton;

    public ButtonFacade(BaseScreen screen) {
        super(screen);
        mFont = new BitmapFont();
        mObserver = new TextObserver();
        mTextButtonStyle = new TextButton.TextButtonStyle();
        mTextButtonStyle.font = mFont;
        mButton = new TextButton(null, mTextButtonStyle);
        mClickHandler = new ChangeHandler();
    }

    @ExposeToLua
    public void setFontColor(int rgba) {
        Color color = new Color(rgba);
        mTextButtonStyle.fontColor = color;
    }

    @ExposeToLua
    public void setButtonDownImage(AssetID innerPath) {
        Drawable drawable =
                mDrawableLoader.getDrawable(mScreen.getAssetLoader().getText(innerPath).get());
        mTextButtonStyle.down = drawable;
    }

    @ExposeToLua
    public void setButtonUpImage(AssetID innerPath) {
        Drawable drawable =
                mDrawableLoader.getDrawable(mScreen.getAssetLoader().getText(innerPath).get());
        mTextButtonStyle.up = drawable;
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);
        mButton.setText(mText.get());
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            mButton.addListener(mClickHandler);
        }else {
            mButton.removeListener(mClickHandler);
        }

    }

    @ExposeToLua
    public void setSize(float width, float height) {
        mButton.setSize(width, height);
    }

    @Override
    public TextButton getActor() {
        return mButton;
    }

    @Override
    public void dispose() {
        if (mText != null && mObserver != null) {
            mText.removeObserver(mObserver);
            mScreen.getAssetLoader().freeAsset(mText);
        }
    }

    private class ChangeHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if(mCallBack != null) {
                mCallBack.call();
            }
        }
    }

    private class TextObserver implements AssetObserver<String> {

        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            mButton.setText(asset.get());
        }
    }
}
