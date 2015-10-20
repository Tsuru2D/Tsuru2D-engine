package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class TextFieldFacade extends UIWrapper<TextField> {
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private BitmapFont mFont;
    private LuaFunction mCallBack;
    private ChangeHandler mChangeHandler;
    private TextField.TextFieldStyle mTextFieldStyle;
    private final TextField mTextField;

    public TextFieldFacade(BaseScreen screen) {
        super(screen);
        mFont = new BitmapFont();
        mTextFieldStyle = new TextField.TextFieldStyle();
        mTextFieldStyle.font = mFont;
        mObserver = new TextObserver();
        mTextField = new TextField("", mTextFieldStyle);
        mChangeHandler = new ChangeHandler();
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            mTextField.addListener(mChangeHandler);
        }else {
            mTextField.removeListener(mChangeHandler);
        }
    }

    @ExposeToLua
    public void setHint(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);
        mTextField.setMessageText(mText.get());
    }

    @ExposeToLua
    public String getText() {
        return mTextField.getText();
    }

    @ExposeToLua
    public void setDisabled(boolean disabled) {
        mTextField.setDisabled(disabled);
    }

    @ExposeToLua
    public boolean isDisabled() {
        return mTextField.isDisabled();
    }

    @ExposeToLua
    public void setPasswordMode(boolean isPasswordMode) {
        mTextField.setPasswordMode(isPasswordMode);
    }

    @ExposeToLua
    public boolean isPasswordMode() {
        return mTextField.isPasswordMode();
    }

    @ExposeToLua
    public void setSize(float width, float heigth) {
        mTextField.setSize(width, heigth);
    }

    @Override
    public TextField getActor() {
        return mTextField;
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
            mTextField.setMessageText(asset.get());
        }
    }

}
