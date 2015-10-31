package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;

public class LabelFacade extends ActorFacade<Label> {
    private ManagedAsset<String> mText;
    private final AssetUpdatedObserver mAssetUpdatedObserver;

    public LabelFacade(BaseScreen screen) {
        super(screen);
        setActor(new Label(null, screen.getSkin()));
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @ExposeToLua
    public void setText(AssetID textID) {
        mText = swapAsset(AssetType.TEXT, textID, mText, mAssetUpdatedObserver);
        if (mText != null) {
            getActor().setText(mText.get());
        } else {
            getActor().setText(null);
        }
    }

    @Override
    public void dispose() {
        mText = freeAsset(mText, mAssetUpdatedObserver);
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            getActor().setText(asset.get());
        }
    }
}
