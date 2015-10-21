package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;

public class LabelFacade extends ActorFacade<Label> {
    private ManagedAsset<String> mText;
    private final AssetUpdatedObserver mAssetUpdatedObserver;

    public LabelFacade(BaseScreen screen) {
        super(screen, new Label(null, screen.getSkin()));
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @ExposeToLua
    public void setText(AssetID text) {
        ManagedAsset<String> asset = mScreen.getAssetLoader().getText(text);
        dispose();
        mText = asset;
        asset.addObserver(mAssetUpdatedObserver);
        mActor.setText(asset.get());
    }

    @Override
    public void dispose() {
        if (mText != null) {
            mText.removeObserver(mAssetUpdatedObserver);
            mScreen.getAssetLoader().freeAsset(mText);
        }
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            mActor.setText(asset.get());
        }
    }
}
