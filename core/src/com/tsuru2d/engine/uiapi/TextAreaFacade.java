package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
<<<<<<< HEAD
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.tsuru2d.engine.BaseScreen;
=======
>>>>>>> d7bdac7fa7d30b6147cb80b93866aaf274199b41
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class TextAreaFacade extends UIWrapper<TextArea> {
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
<<<<<<< HEAD
    private BitmapFont mFont;
    private TextField.TextFieldStyle mTextFieldStyle;
    private final TextArea mTextArea;

    public TextAreaFacade(BaseScreen screen, LuaTable data) {
        mTextFieldStyle = new TextField.TextFieldStyle();
        mFont = new BitmapFont();
        mTextFieldStyle.font = mFont;
        mScreen = screen;
=======
    private LuaFunction mOnClickCallback;
    private final TextArea mTextArea;

    public TextAreaFacade(LuaTable data) {
>>>>>>> d7bdac7fa7d30b6147cb80b93866aaf274199b41
        mLuaTable = data;
        mObserver = new TextObserver();
        mTextArea = new TextArea("", mTextFieldStyle);
    }

    @ExposeToLua
    private void setMessage(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mTextArea.setMessageText(mText.get());
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

    @ExposeToLua
    public void setSize(float width, float hight) {
        mTextArea.setSize(width, hight);
    }

    @Override
    @ExposeToLua
    public void setPosition(float x, float y) {
        mTextArea.setPosition(x, y);
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
            mTextArea.setMessageText(asset.get());
        }
    }

}
