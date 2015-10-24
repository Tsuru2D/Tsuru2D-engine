package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.LuaAssetID;
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
    public void setText(LuaAssetID textID) {
        ManagedAsset<String> newText = mScreen.getAssetLoader().getText(textID.userdata());
        dispose();
        mText = newText;
        newText.addObserver(mAssetUpdatedObserver);
        mActor.setText(newText.get());
    }

    @Override
    public void dispose() {
        if (mText != null) {
            mText.removeObserver(mAssetUpdatedObserver);
            mScreen.getAssetLoader().freeAsset(mText);
            mText = null;
        }
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            mActor.setText(asset.get());
        }
    }
}
