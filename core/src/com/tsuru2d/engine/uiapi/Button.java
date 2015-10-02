package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import org.luaj.vm2.LuaTable;

public class Button {
    private BaseScreen mScreen;
    private TextButton mButton;
    private ManagedAsset<String> mText;
    private TextObserver observer;

    public Button(BaseScreen screen, LuaTable data) {
        mScreen = screen;
        observer = new TextObserver();
        mButton = new TextButton(null, new TextButton.TextButtonStyle());
    }

    public void setText(AssetID text) {
        if (mText != null && observer != null) {
            mText.removeObserver(observer);
            mScreen.getAssetLoader().freeAsset(mText);
        }
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(observer);
    }

    public void dispose() {
        if (mText != null) {
            mText.removeObserver(observer);
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
