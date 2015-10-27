package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class TextFieldFacade extends ActorFacade<TextField>{
    private ManagedAsset<Texture> mBackground, mCursor, mSelection,
                                    mDisabledBackground, mFocusedBackground;
    private ManagedAsset<String> mHint;
    private ChangeHandler mChangeHandler;
    private LuaFunction mCallBack;
    private final AssetUpdatedObserver mAssetUpdatedObserver;

    public TextFieldFacade (BaseScreen baseScreen) {
        super(baseScreen);
        TextField textField = new TextField("", getStyle());
        setActor(textField);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
        mChangeHandler = new ChangeHandler();
    }

    public TextField.TextFieldStyle getStyle() {
        TextField.TextFieldStyle textFieldStyle = new TextField.TextFieldStyle();
        textFieldStyle.background =
                mBackground == null ? null : new TextureRegionDrawable(new TextureRegion(mBackground.get()));
        textFieldStyle.cursor =
                mCursor == null ? null : new TextureRegionDrawable(new TextureRegion(mCursor.get()));
        textFieldStyle.selection =
                mSelection == null ? null : new TextureRegionDrawable(new TextureRegion(mSelection.get()));
        textFieldStyle.disabledBackground =
                mDisabledBackground == null ? null : new TextureRegionDrawable(new TextureRegion(mDisabledBackground.get()));
        textFieldStyle.focusedBackground =
                mFocusedBackground == null ? null : new TextureRegionDrawable(new TextureRegion(mFocusedBackground.get()));
        return textFieldStyle;
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        dispose();
        mBackground = getStyleAsset(styleTable, "background");
        mCursor = getStyleAsset(styleTable, "cursor");
        mSelection = getStyleAsset(styleTable, "selection");
        mDisabledBackground = getStyleAsset(styleTable, "disabledbackground");
        mFocusedBackground = getStyleAsset(styleTable, "focusedbackground");
        TextField.TextFieldStyle textFieldStyle = getStyle();
        mActor.setStyle(textFieldStyle);
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
    public void setOnClick(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            getActor().addListener(mChangeHandler);
        }else {
            getActor().removeListener(mChangeHandler);
        }
    }

    @ExposeToLua
    public void setHint(AssetID hint) {
        ManagedAsset<String> text = mScreen.getAssetLoader().getText(hint);
        if (mHint != null) {
            mHint.removeObserver(mAssetUpdatedObserver);
            mScreen.getAssetLoader().freeAsset(mHint);
            mHint = null;
        }
        mHint = text;
        mHint.addObserver(mAssetUpdatedObserver);
        getActor().setText(mHint.get());
    }

    @ExposeToLua
    public String getString() {
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
        if(mBackground != null) {mScreen.getAssetLoader().freeAsset(mBackground); mBackground = null;}
        if(mCursor != null) {mScreen.getAssetLoader().freeAsset(mCursor); mCursor = null;}
        if(mSelection != null) {mScreen.getAssetLoader().freeAsset(mSelection); mSelection = null;}
        if(mDisabledBackground != null) {
            mScreen.getAssetLoader().freeAsset(mDisabledBackground); mDisabledBackground = null;
        }
        if(mFocusedBackground != null) {
            mScreen.getAssetLoader().freeAsset(mFocusedBackground); mFocusedBackground = null;
        }
    }

    private class ChangeHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if(mCallBack != null) {
                mCallBack.call();
            }
        }
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            getActor().setText(asset.get());
        }
    }

}
