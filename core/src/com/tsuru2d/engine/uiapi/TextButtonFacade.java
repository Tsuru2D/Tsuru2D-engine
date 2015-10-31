package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class TextButtonFacade extends ButtonFacade {
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<String> mText;

    public TextButtonFacade(BaseScreen screen) {
        super(screen);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @Override
    protected Button createActor() {
        return new TextButton(null, (TextButton.TextButtonStyle)createStyle());
    }

    @Override
    protected Button.ButtonStyle createStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = new BitmapFont();
        return style;
    }

    @Override
    protected void populateStyle(Button.ButtonStyle style, LuaTable styleTable) {
        super.populateStyle(style, styleTable);
        TextButton.TextButtonStyle realStyle = (TextButton.TextButtonStyle)style;
        realStyle.fontColor = tableToColor(styleTable.get("textColor"));
    }

    @ExposeToLua
    public void setText(AssetID textID) {
        mText = swapAsset(AssetType.TEXT, textID, mText, mAssetUpdatedObserver);
        TextButton textButton = (TextButton)getActor();
        if (mText != null) {
            textButton.setText(mText.get());
        } else {
            textButton.setText(null);
        }
    }

    @Override
    public void dispose() {
        freeAsset(mText, mAssetUpdatedObserver);
        super.dispose();
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            ((TextButton)getActor()).setText(asset.get());
        }
    }
}
