package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class TextAreaFacade implements UIWrapper<TextArea> {
    private BaseScreen mScreen;
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private final LuaTable mLuaTable;
    private LuaFunction mOnClickCallback;
    private final TextArea mTextArea;

    public TextAreaFacade(BaseScreen screen, LuaTable data) {
        mScreen = screen;
        mLuaTable = data;
        mObserver = new TextObserver();
        mTextArea = new TextArea("", new Skin());
    }

    @ExposeToLua
    private void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mTextArea.setText(mText.get());
        mText.addObserver(mObserver);
    }

    @ExposeToLua
    public String getText() {
        return mTextArea.getText();
    }


    @ExposeToLua
    public void setEnabled(boolean enabled) {
        mTextArea.setDisabled(!enabled);
    }

    @ExposeToLua
    public boolean isEnabled() {
        return !mTextArea.isDisabled();
    }

    @Override
    public TextArea getActor() {
        return mTextArea;
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
            mTextArea.setText(asset.get());
        }
    }

}
