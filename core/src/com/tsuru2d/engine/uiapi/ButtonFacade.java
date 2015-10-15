package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;

import org.luaj.vm2.LuaTable;

public class ButtonFacade extends ButtonSuper {
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private ClickHandler mClickHandler;
    private LuaFunction mCallBack;
    private TextButton.TextButtonStyle mTextButtonStyle;
    private BitmapFont mFont;
    private final TextButton mButton;

    public ButtonFacade(BaseScreen screen, LuaTable luaTable) {
        super(screen, luaTable);
        mFont = new BitmapFont();
        mObserver = new TextObserver();
        mTextButtonStyle = new TextButton.TextButtonStyle();
        mTextButtonStyle.font = mFont;
        mButton = new TextButton(null, mTextButtonStyle);
        mClickHandler = new ClickHandler();
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);

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

    private class ClickHandler extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
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
