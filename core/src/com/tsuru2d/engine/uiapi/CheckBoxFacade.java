package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class CheckBoxFacade extends ActorFacade<CheckBox, CheckBox.CheckBoxStyle> {
    private final AssetUpdatedObserver mAssetUpdatedObserver;
    private ManagedAsset<Texture> mCheckboxOff, mCheckboxOffDisabled,
                                  mCheckboxOn, mCheckboxOnDisabled, mCheckboxOver;
    private ManagedAsset<String> mText;
    private LuaFunction mCheckedChangedCallback;

    public CheckBoxFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
    }

    @Override
    protected CheckBox createActor(CheckBox.CheckBoxStyle style) {
        return new CheckBox(null, style);
    }

    @Override
    protected void initializeActor(CheckBox actor) {
        actor.addListener(new CheckedChangedHandler());
    }

    @Override
    protected CheckBox.CheckBoxStyle createStyle() {
        CheckBox.CheckBoxStyle style = new CheckBox.CheckBoxStyle();
        style.font = new BitmapFont();
        return style;
    }

    @Override
    protected void populateStyle(CheckBox.CheckBoxStyle style, LuaTable styleTable) {
        mCheckboxOff = swapStyleImage(styleTable, "off", mCheckboxOff);
        mCheckboxOffDisabled = swapStyleImage(styleTable, "offDisabled", mCheckboxOffDisabled);
        mCheckboxOn = swapStyleImage(styleTable, "on", mCheckboxOn);
        mCheckboxOnDisabled = swapStyleImage(styleTable, "onDisabled", mCheckboxOnDisabled);
        mCheckboxOver = swapStyleImage(styleTable, "over", mCheckboxOver);

        style.checkboxOff = toDrawable(mCheckboxOff);
        style.checkboxOffDisabled = toDrawable(mCheckboxOffDisabled);
        style.checkboxOn = toDrawable(mCheckboxOn);
        style.checkboxOnDisabled = toDrawable(mCheckboxOnDisabled);
        style.over = toDrawable(mCheckboxOver);
        style.fontColor = tableToColor(styleTable.get("textColor"));
    }

    @ExposeToLua
    public void setCheckedChangedListener(LuaFunction callback) {
        mCheckedChangedCallback = callback;
    }

    @ExposeToLua
    public void setEnabled(boolean enabled) {
        getActor().setDisabled(!enabled);
    }

    @ExposeToLua
    public boolean isEnabled() {
        return !getActor().isDisabled();
    }

    @ExposeToLua
    public void setChecked(boolean checked) {
        getActor().setChecked(checked);
    }

    @ExposeToLua
    public boolean isChecked() {
        return getActor().isChecked();
    }

    @ExposeToLua
    public void setText(AssetID textID) {
        mText = swapAsset(AssetType.TEXT, textID, mText, mAssetUpdatedObserver);
        if (mText != null) {
            getActor().setText(mText.get());
        } else {
            getActor().setText(null);
        }
    }

    @Override
    public void dispose() {
        mCheckboxOff = freeAsset(mCheckboxOff);
        mCheckboxOffDisabled = freeAsset(mCheckboxOffDisabled);
        mCheckboxOn = freeAsset(mCheckboxOn);
        mCheckboxOnDisabled = freeAsset(mCheckboxOnDisabled);
        mCheckboxOver = freeAsset(mCheckboxOver);
        mText = freeAsset(mText, mAssetUpdatedObserver);
        super.dispose();
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            getActor().setText(asset.get());
        }
    }

    private class CheckedChangedHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mCheckedChangedCallback != null) {
                mCheckedChangedCallback.call(CheckBoxFacade.this);
            }
        }
    }
}
