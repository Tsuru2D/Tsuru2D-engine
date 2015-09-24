package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetLoader;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;

public class Button implements AssetObserver<String> {
    private TextButton mButton;
    private AssetLoader mAssetLoader;
    private ManagedAsset<String> mText;

    public void setText(AssetID text) {
        if (mText != null) {
            mAssetLoader.freeAsset(mText);
        }
        mText = mAssetLoader.getText(text);
        mText.addObserver(this);
    }

    @Override
    public void onAssetUpdated(ManagedAsset<String> asset) {
        mButton.setText(asset.get());
    }

    public void dispose() {
        mText.removeObserver(this);
        mAssetLoader.freeAsset(mText);
    }
}
