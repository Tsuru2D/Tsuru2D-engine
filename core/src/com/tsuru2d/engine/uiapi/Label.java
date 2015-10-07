package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class Label extends ClickListener implements UIWrapper {
    private BaseScreen mScreen;
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private final LuaTable mLuaTable;
    private LuaFunction mOnClickCallback;
    private final com.badlogic.gdx.scenes.scene2d.ui.Label mLabel;

    public Label(BaseScreen screen, LuaTable data) {
        mLuaTable = data;
        mScreen = screen;
        mObserver = new TextObserver();
        mLabel = new com.badlogic.gdx.scenes.scene2d.ui.Label(null, mScreen.getSkin());
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if (mOnClickCallback != null) {
            mOnClickCallback.call();
        }
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callback) {
        mOnClickCallback = callback;
        if (callback != null) {
            mLabel.addListener(this);
        } else {
            mLabel.removeListener(this);
        }
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);
    }

    @Override
    public com.badlogic.gdx.scenes.scene2d.ui.Label getActor() {
        return mLabel;
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
            mLabel.setText(asset.get());
        }
    }
}
