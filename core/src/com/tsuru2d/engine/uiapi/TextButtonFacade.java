package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.LuaUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.Varargs;

public class TextButtonFacade extends ButtonFacade {
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<String> mText;
    private Object[] mFormatParams;

    public TextButtonFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @Override
    public TextButton getActor() {
        return (TextButton)super.getActor();
    }

    @Override
    protected Button createActor(Button.ButtonStyle style) {
        return new TextButton(null, (TextButton.TextButtonStyle)style);
    }

    @Override
    protected Button.ButtonStyle createStyle() {
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.font = FontFactory.font24();
        return style;
    }

    @Override
    protected void populateStyle(Button.ButtonStyle style, LuaTable styleTable) {
        super.populateStyle(style, styleTable);
        TextButton.TextButtonStyle realStyle = (TextButton.TextButtonStyle)style;
        realStyle.fontColor = tableToColor(styleTable.get(TEXT_COLOR));
    }

    @ExposeToLua
    public void setText(AssetID textID, Varargs args) {
        mFormatParams = (args == null) ? null : LuaUtils.toArray(args);
        mText = swapAsset(AssetType.TEXT, textID, mText, mAssetUpdatedObserver);
        if (mText != null) {
            mText.touch();
        } else {
            updateText();
        }
    }

    private void updateText() {
        String text = getText(mText, mFormatParams);
        TextButton button = getActor();
        button.setText(text);
    }

    @Override
    public void dispose() {
        mText = freeAsset(mText, mAssetUpdatedObserver);
        mFormatParams = null;
        super.dispose();
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            updateText();
        }
    }
}
