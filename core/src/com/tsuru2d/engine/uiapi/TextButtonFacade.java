package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

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
        LuaValue colorValue = styleTable.get("textColor");
        if (!colorValue.isnil()) {
            LuaTable colorTable = colorValue.checktable();
            float r = getfloat(colorTable, 1);
            float g = getfloat(colorTable, 2);
            float b = getfloat(colorTable, 3);
            float a = getoptfloat(colorTable, 4, 1);
            Color color = new Color(r, g, b, a);
            ((TextButton.TextButtonStyle)style).fontColor = color;
        }
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
        super.dispose();
    }

    private static float checkfloat(LuaValue value) {
        return (float)value.checkdouble();
    }

    private static float getfloat(LuaTable table, int index) {
        LuaValue value = table.get(index);
        return checkfloat(value);
    }

    private static float getoptfloat(LuaTable table, int index, float defaultValue) {
        LuaValue value = table.get(index);
        if (value.isnil()) {
            return defaultValue;
        }
        return checkfloat(value);
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            ((TextButton)mActor).setText(asset.get());
        }
    }
}
