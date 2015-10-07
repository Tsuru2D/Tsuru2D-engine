package com.tsuru2d.engine.uiapi;

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

public class Button extends ClickListener implements UIWrapper<TextButton> {
    private BaseScreen mScreen;
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private final LuaTable mLuaTable;
    private LuaFunction mCallBack;
    private final TextButton mButton;

    public Button(BaseScreen screen, LuaTable data) {
        mLuaTable = data;
        mScreen = screen;
        mObserver = new TextObserver();
        mButton = new TextButton(null, new TextButton.TextButtonStyle());
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
            mButton.addListener(this);
        }else {
            mButton.removeListener(this);
        }

    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if(mCallBack != null) {
            mCallBack.call();
        }
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

    private class TextObserver implements AssetObserver<String> {

        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            mButton.setText(asset.get());
        }
    }
}
