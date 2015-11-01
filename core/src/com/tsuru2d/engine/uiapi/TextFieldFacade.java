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
import org.luaj.vm2.LuaValue;

public class TextFieldFacade extends ActorFacade<TextField, TextField.TextFieldStyle> {
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<Texture> mBackground, mCursor, mSelection,
                                  mDisabledBackground, mFocusedBackground;
    private ManagedAsset<String> mHint;
    private LuaFunction mTextChangedCallback;

    public TextFieldFacade(BaseScreen baseScreen, AssetID styleID) {
        super(baseScreen, styleID);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @Override
    protected TextField createActor(TextField.TextFieldStyle style) {
        return new TextField(null, style);
    }

    @Override
    protected void initializeActor(TextField actor) {
        actor.addListener(new TextChangedHandler());
        // The default password character (bullet) does not exist
        // within the default font, and for some reason libGDX decides
        // that if the character is not displayable, it should
        // show the password in plaintext...
        actor.setPasswordCharacter('*');
    }

    @Override
    protected TextField.TextFieldStyle createStyle() {
        TextField.TextFieldStyle style = new TextField.TextFieldStyle();
        style.font = new BitmapFont();
        return style;
    }

    @Override
    protected void populateStyle(TextField.TextFieldStyle style, LuaTable styleTable) {
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
    public void setPasswordMode(boolean isPasswordMode) {
        getActor().setPasswordMode(isPasswordMode);
    }

    @ExposeToLua
    public boolean isPasswordMode() {
        return getActor().isPasswordMode();
    }

    @ExposeToLua
    public void setTextChangedListener(LuaFunction callback) {
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
        super.dispose();
    }

    private class TextChangedHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mTextChangedCallback != null) {
                mTextChangedCallback.call(TextFieldFacade.this, LuaValue.valueOf(getText()));
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
