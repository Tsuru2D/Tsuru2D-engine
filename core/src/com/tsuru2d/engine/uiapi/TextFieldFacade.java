package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class TextFieldFacade extends ActorFacade<TextField>{
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<Texture> mBackground, mCursor, mSelection,
                                  mDisabledBackground, mFocusedBackground;
    private ManagedAsset<String> mHint;
    private LuaFunction mTextChangedCallback;

    public TextFieldFacade (BaseScreen baseScreen) {
        super(baseScreen);
        TextField textField = new TextField(null, createStyle());
        textField.addListener(new ChangeHandler());
        setActor(textField);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    private TextField.TextFieldStyle createStyle() {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        return style;
    }

    private void populateStyle(TextField.TextFieldStyle style, LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, "background", mBackground);
        mCursor = swapStyleImage(styleTable, "cursor", mCursor);
        mSelection = swapStyleImage(styleTable, "selection", mSelection);
        mDisabledBackground = swapStyleImage(styleTable, "disabledBackground", mDisabledBackground);
        mFocusedBackground = swapStyleImage(styleTable, "focusedBackground", mFocusedBackground);

        style.background = toDrawable(mBackground);
        style.cursor = toDrawable(mCursor);
        style.selection = toDrawable(mSelection);
        style.disabledBackground = toDrawable(mDisabledBackground);
        style.focusedBackground = toDrawable(mFocusedBackground);
        style.fontColor = tableToColor(styleTable.get("textColor"));
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        TextField.TextFieldStyle style = createStyle();
        populateStyle(style, styleTable);
        getActor().setStyle(style);
    }

    @ExposeToLua
    public void setPasswordMode(boolean isPasswordMode) {
        getActor().setPasswordMode(isPasswordMode);
    }

    @ExposeToLua
    public boolean isPasswordMode() {
        return getActor().isPasswordMode();
    }

    @ExposeToLua
    public void setOnTextChangedListener(LuaFunction callback) {
        mTextChangedCallback = callback;
    }

    @ExposeToLua
    public void setHint(AssetID hintTextID) {
        mHint = swapAsset(AssetType.TEXT, hintTextID, mHint, mAssetUpdatedObserver);
        if (mHint != null) {
            getActor().setMessageText(mHint.get());
        } else {
            getActor().setMessageText(null);
        }
    }

    @ExposeToLua
    public void setText(String text) {
        getActor().setText(text);
    }

    @ExposeToLua
    public String getText() {
        return getActor().getText();
    }

    @ExposeToLua
    public void setDisabled(boolean disabled) {
        getActor().setDisabled(disabled);
    }

    @ExposeToLua
    public boolean isDisabled() {
        return getActor().isDisabled();
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        mCursor = freeAsset(mCursor);
        mSelection = freeAsset(mSelection);
        mDisabledBackground = freeAsset(mDisabledBackground);
        mFocusedBackground = freeAsset(mFocusedBackground);
        mHint = freeAsset(mHint, mAssetUpdatedObserver);
    }

    private class ChangeHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mTextChangedCallback != null) {
                mTextChangedCallback.call(TextFieldFacade.this);
            }
        }
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            getActor().setMessageText(asset.get());
        }
    }
}
