package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;

public class LabelFacade extends UIWrapper<Label> {
    private ManagedAsset<String> mText;
    private TextObserver mObserver;
    private Label.LabelStyle mLabelStyle;
    private final Label mLabel;

    public LabelFacade(BaseScreen screen) {
        super(screen);
        mLabelStyle = new Label.LabelStyle();
        mLabelStyle.font = new BitmapFont();
        mObserver = new TextObserver();
        mLabel = new Label(null, new Skin());
    }

    @ExposeToLua
    public void setText(AssetID text) {
        dispose();
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(mObserver);
    }

    @ExposeToLua
    public void setFontColor(int rgba) {
        mLabelStyle.fontColor =new Color(rgba);
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

    private class TextObserver implements AssetObserver<String> {

        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            mLabel.setText(asset.get());
        }
    }
}
