package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import org.luaj.vm2.LuaTable;

public class Button implements AssetObserver<String> {
    private BaseScreen mScreen;
    private TextButton mButton;
    private ManagedAsset<String> mText;

    public Button(BaseScreen screen, LuaTable data) {
        mScreen = screen;
        mButton = new TextButton(null, new TextButton.TextButtonStyle());
    }

    public void setText(AssetID text) {
        if (mText != null) {
            mText.removeObserver(this);
            mScreen.getAssetLoader().freeAsset(mText);
        }
        mText = mScreen.getAssetLoader().getText(text);
        mText.addObserver(this);
    }

    @Override
    public void onAssetUpdated(ManagedAsset<String> asset) {
        mButton.setText(asset.get());
    }

    public void dispose() {
        if (mText != null) {
            mText.removeObserver(this);
            mScreen.getAssetLoader().freeAsset(mText);
        }
    }
}
