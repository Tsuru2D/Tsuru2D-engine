package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class LabelFacade extends UIWrapper<Label> {
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private ClickHandler mClickHandler;
    private LuaFunction mCallBack;
    private Label.LabelStyle mLabelStyle;
    private BitmapFont mFont;
    private final Label mLabel;

    public LabelFacade(BaseScreen screen, LuaTable data) {
        super(screen, data);
        mFont = new BitmapFont();
        mLabelStyle = new Label.LabelStyle();
        mLabelStyle.font = mFont;
        mObserver = new TextObserver();
        mLabel = new Label(null, new Skin());
        mClickHandler = new ClickHandler();
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callback) {
        mCallBack = callback;
        if (callback != null) {
            mLabel.addListener(mClickHandler);
        } else {
            mLabel.removeListener(mClickHandler);
        }
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);
    }

    @Override
    @ExposeToLua
    public void setPosition(float x, float y) {
        mLabel.setPosition(x, y);
    }

    @Override
    public Label getActor() {
        return mLabel;
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
            mLabel.setText(asset.get());
        }
    }
}
