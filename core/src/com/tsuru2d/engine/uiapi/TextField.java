package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class TextField implements UIWrapper{
    private BaseScreen mScreen;
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private final LuaTable mLuaTable;
    private LuaFunction mOnClickCallback;
    private final com.badlogic.gdx.scenes.scene2d.ui.TextField mTextField;

    public TextField(BaseScreen screen, LuaTable data) {
        mScreen = screen;
        mLuaTable = data;
        mObserver = new TextObserver();
        mTextField = new com.badlogic.gdx.scenes.scene2d.ui.TextField("", mScreen.getSkin());
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);
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
    public Actor getActor() {
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
            mTextField.setText(asset.get());
        }
    }

}