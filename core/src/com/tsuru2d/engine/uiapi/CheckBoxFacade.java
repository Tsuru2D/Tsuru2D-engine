package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class CheckBoxFacade extends UIWrapper<CheckBox> {
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private BitmapFont mFont;
    private CheckBox.CheckBoxStyle mCheckBoxStyle;
    private final CheckBox mCheckBox;

    public CheckBoxFacade(LuaTable data) {
        mFont = new BitmapFont();
        mCheckBoxStyle = new CheckBox.CheckBoxStyle();
        mCheckBoxStyle.font = mFont;
        mLuaTable = data;
        mObserver = new TextObserver();
        mCheckBox = new CheckBox("", mCheckBoxStyle);
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mCheckBox.setText(mText.get());
        mText.addObserver(mObserver);
    }

    @ExposeToLua
    public boolean isChecked() {
        return mCheckBox.isChecked();
    }

    @Override
    @ExposeToLua
    public void setPosition(float x, float y) {
        mCheckBox.setPosition(x, y);
    }

    @ExposeToLua
    public void setChecked(boolean checked) {
        mCheckBox.setChecked(checked);
    }

    @Override
    public CheckBox getActor() {
        return mCheckBox;
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
            mCheckBox.setText(asset.get());
        }
    }
}