package com.tsuru2d.engine.uiapi;


import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;

public class TextButtonFacade extends ButtonFacade {
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<String> mText;

    public TextButtonFacade(BaseScreen screen) {
        super(screen);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @Override
    protected Button createActor() {
        return new TextButton(null, (TextButton.TextButtonStyle)createAndPopulateStyle());
    }

    @Override
    protected Button.ButtonStyle createStyle() {
        return new TextButton.TextButtonStyle();
    }

    @Override
    protected Button.ButtonStyle createAndPopulateStyle() {
        Button.ButtonStyle style = super.createAndPopulateStyle();
        ((TextButton.TextButtonStyle)style).font = new BitmapFont();
        return style;
    }

    @ExposeToLua
    public void setText(AssetID textID) {
        // Load the new asset before disposing the old one, to make sure
        // that the raw asset is not unloaded and then immediately reloaded
        ManagedAsset<String> newText = mScreen.getAssetLoader().getText(textID);
        dispose();
        mText = newText;
        newText.addObserver(mAssetUpdatedObserver);
        ((TextButton)mActor).setText(newText.get());
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
            ((TextButton)mActor).setText(asset.get());
        }
    }
}
