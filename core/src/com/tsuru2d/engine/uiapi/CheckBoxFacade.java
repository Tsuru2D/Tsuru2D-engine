package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class CheckBoxFacade extends ActorFacade<CheckBox> {
    private ManagedAsset<Texture> mCheckboxOff, mCheckboxOffDisabled, mCheckboxOn,
                                    mCheckboxOnDisabled, mCheckboxOver;
    private ManagedAsset<String> mText;
    private ChangeHandler mChangeHandler;
    private LuaFunction mCallBack;
    private final AssetUpdatedObserver mAssetUpdatedObserver;

    public CheckBoxFacade(BaseScreen screen) {
        super(screen);
        CheckBox checkBox = new CheckBox("", getStyle());
        setActor(checkBox);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
        mChangeHandler = new ChangeHandler();
    }

    public CheckBox.CheckBoxStyle getStyle() {
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.checkboxOff =
                mCheckboxOff == null ? null : new TextureRegionDrawable(new TextureRegion(mCheckboxOff.get()));
        style.checkboxOn =
                mCheckboxOn == null ? null : new TextureRegionDrawable(new TextureRegion(mCheckboxOn.get()));
        style.checkboxOver =
                mCheckboxOver == null ? null : new TextureRegionDrawable(new TextureRegion(mCheckboxOver.get()));
        style.checkboxOffDisabled =
                mCheckboxOffDisabled == null ? null : new TextureRegionDrawable(new TextureRegion(mCheckboxOffDisabled.get()));
        style.checkboxOnDisabled =
                mCheckboxOnDisabled == null ? null : new TextureRegionDrawable(new TextureRegion(mCheckboxOnDisabled.get()));
        //Temporary Code
        style.font = new BitmapFont();
        style.fontColor = Color.BLUE;
        return style;
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        dispose();
        mCheckboxOff = getStyleAsset(styleTable, "checkboxoff");
        mCheckboxOffDisabled = getStyleAsset(styleTable, "checkboxoffdisabled");
        mCheckboxOn = getStyleAsset(styleTable, "checkboxon");
        mCheckboxOnDisabled = getStyleAsset(styleTable, "checkboxondisabled");
        mCheckboxOver = getStyleAsset(styleTable, "checkboxover");
        CheckBox.CheckBoxStyle checkBoxStyle = getStyle();
        getActor().setStyle(checkBoxStyle);
    }

    @ExposeToLua
    public void setChangeListener(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            getActor().addListener(mChangeHandler);
        }else {
            getActor().removeListener(mChangeHandler);
        }
    }

    @ExposeToLua
    public void setDisabled(boolean disabled) {
        getActor().setDisabled(disabled);
    }

    @ExposeToLua
    public boolean isDisabled() {
        return getActor().isDisabled();
    }

    @ExposeToLua
    public boolean isChecked() {
        return getActor().isChecked();
    }

    @ExposeToLua
    public void setText(AssetID hint) {
        ManagedAsset<String> text = mScreen.getAssetLoader().getText(hint);
        if (mText != null) {
            mText.removeObserver(mAssetUpdatedObserver);
            mScreen.getAssetLoader().freeAsset(mText);
            mText = null;
        }
        mText = text;
        mText.addObserver(mAssetUpdatedObserver);
        getActor().setText(mText.get());
    }

    @Override
    public void dispose() {
        if (mCheckboxOff != null) {
            mScreen.getAssetLoader().freeAsset(mCheckboxOff);
            mCheckboxOff = null;
        }
        if (mCheckboxOffDisabled != null) {
            mScreen.getAssetLoader().freeAsset(mCheckboxOffDisabled);
            mCheckboxOffDisabled = null;
        }
        if (mCheckboxOn != null) {
            mScreen.getAssetLoader().freeAsset(mCheckboxOn);
            mCheckboxOn = null;
        }
        if (mCheckboxOnDisabled != null) {
            mScreen.getAssetLoader().freeAsset(mCheckboxOnDisabled);
            mCheckboxOnDisabled = null;
        }
        if (mCheckboxOver != null) {
            mScreen.getAssetLoader().freeAsset(mCheckboxOver);
            mCheckboxOver = null;
        }
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            getActor().setText(asset.get());
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
}
