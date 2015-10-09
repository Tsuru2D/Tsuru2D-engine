package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class CheckBoxFacade implements UIWrapper<CheckBox> {
    private BaseScreen mScreen;
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private final LuaTable mLuaTable;
    private final CheckBox mCheckBox;

    public CheckBoxFacade(BaseScreen screen, LuaTable data) {
        mLuaTable = data;
        mScreen = screen;
        mObserver = new TextObserver();
        mCheckBox = new CheckBox("", new Skin());
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
