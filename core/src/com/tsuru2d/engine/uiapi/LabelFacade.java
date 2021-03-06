package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.LuaUtils;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.Varargs;

public class LabelFacade extends ActorFacade<Label, Label.LabelStyle> {
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<Texture> mBackground;
    private ManagedAsset<String> mText;
    private Object[] mFormatParams;

    public LabelFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @Override
    protected Label createActor(Label.LabelStyle style) {
        return new Label(null, style);
    }

    @Override
    protected Label.LabelStyle createStyle() {
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = FontFactory.font24();
        return style;
    }

    @Override
    protected void populateStyle(Label.LabelStyle style, LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, BACKGROUND, mBackground);

        style.background = toDrawable(mBackground);
        style.fontColor = tableToColor(styleTable.get(TEXT_COLOR));
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
        getActor().setText(text);
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
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
