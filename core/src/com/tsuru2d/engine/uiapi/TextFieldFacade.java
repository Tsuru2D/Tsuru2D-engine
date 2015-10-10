package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class TextFieldFacade extends UIWrapper<TextField> {
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private final TextField mTextField;

    public TextFieldFacade(LuaTable data) {
        mLuaTable = data;
        mObserver = new TextObserver();
        mTextField = new TextField("", new Skin());
    }

    @ExposeToLua
    public void setMessage(AssetID text) {
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
    public void Disable() {
        mTextField.setDisabled(true);
    }

    @ExposeToLua
    public void Enable() {
        mTextField.setDisabled(false);
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

    private class TextObserver implements AssetObserver<String> {

        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            mTextField.setMessageText(asset.get());
        }
    }

}
