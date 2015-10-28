package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class TextAreaFacade extends ActorFacade<TextArea>{
    private ManagedAsset<Texture> mBackground, mCursor, mSelection,
        mDisabledBackground, mFocusedBackground;
    private ManagedAsset<String> mHint;
    private ChangeHandler mChangeHandler;
    private LuaFunction mCallBack;
    private final AssetUpdatedObserver mAssetUpdatedObserver;

    public TextAreaFacade (BaseScreen baseScreen) {
        super(baseScreen);
        TextArea textArea = new TextArea("", getStyle());
        setActor(textArea);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
        mChangeHandler = new ChangeHandler();
    }

    public TextArea.TextFieldStyle getStyle() {
        TextArea.TextFieldStyle textFieldStyle = new TextArea.TextFieldStyle();
        textFieldStyle.background = getDrawable(mBackground);
        textFieldStyle.cursor = getDrawable(mCursor);
        textFieldStyle.selection = getDrawable(mSelection);
        textFieldStyle.disabledBackground = getDrawable(mDisabledBackground);
        textFieldStyle.focusedBackground = getDrawable(mFocusedBackground);
        return textFieldStyle;
    }

    private TextureRegionDrawable getDrawable(ManagedAsset<Texture> texture) {
        return (texture==null)? null:new TextureRegionDrawable(new TextureRegion(texture.get()));
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        dispose();
        mBackground = getStyleAsset(styleTable, "background");
        mCursor = getStyleAsset(styleTable, "cursor");
        mSelection = getStyleAsset(styleTable, "selection");
        mDisabledBackground = getStyleAsset(styleTable, "disabledbackground");
        mFocusedBackground = getStyleAsset(styleTable, "focusedbackground");
        TextArea.TextFieldStyle textFieldStyle = getStyle();
        mActor.setStyle(textFieldStyle);
    }


    @ExposeToLua
    public void setOnTextChange(LuaFunction callBack) {
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
        dispose(mBackground);
        dispose(mCursor);
        dispose(mSelection);
        dispose(mDisabledBackground);
        dispose(mFocusedBackground);
    }

    private void dispose(ManagedAsset asset){
        if(asset!=null) {
            mScreen.getAssetLoader().freeAsset(asset);
            asset=null;
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
