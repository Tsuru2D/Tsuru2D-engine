package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.LuaArrayIterator;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class DropDownFacade extends ActorFacade<SelectBox<DropDownFacade.Item>, SelectBox.SelectBoxStyle> {
    private ManagedAsset<Texture> mBackground, mDropdownBackground, mDropdownSelectionBackground,
                                  mBackgroundDisabled, mBackgroundOpen, mBackgroundOver;
    private LuaFunction mChangedCallback;
    private Array<Item> mItems;
    private AssetUpdatedObserver mAssetUpdatedObserver;

    public DropDownFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
        mAssetUpdatedObserver = new AssetUpdatedObserver();
        mItems = new Array<Item>();
    }

    @Override
    protected SelectBox<Item> createActor(SelectBox.SelectBoxStyle style) {
        return new SelectBox<Item>(style);
    }

    @Override
    protected SelectBox.SelectBoxStyle createStyle() {
        return new SelectBox.SelectBoxStyle();
    }

    @Override
    protected void initializeActor(SelectBox<Item> actor) {
        actor.addListener(new ChangeHandler());
    }

    @Override
    protected void populateStyle(SelectBox.SelectBoxStyle style, LuaTable styleTable) {
        // Background image of the box
        mBackground = swapStyleImage(styleTable, "background", mBackground);
        // Background image of the box when the control is disabled
        mBackgroundDisabled = swapStyleImage(styleTable, "backgroundDisabled", mBackgroundDisabled);
        // Background image of the box when the dropdown list is visible
        mBackgroundOpen = swapStyleImage(styleTable, "backgroundOpen", mBackgroundOpen);
        // Background image of the box when the mouse is hovered over it
        mBackgroundOver = swapStyleImage(styleTable, "backgroundOver", mBackgroundOver);
        // Background image of the dropdown list
        mDropdownBackground = swapStyleImage(styleTable, "dropdownBackground", mDropdownBackground);
        // Background image of the highlighted item in the dropdown list
        mDropdownSelectionBackground = swapStyleImage(styleTable, "dropdownSelectionBackground", mDropdownSelectionBackground);

        List.ListStyle listStyle = new List.ListStyle();
        listStyle.font = new BitmapFont();
        listStyle.fontColorSelected = tableToColor(styleTable.get("dropdownTextColorSelected"));
        listStyle.fontColorUnselected = tableToColor(styleTable.get("dropdownTextColorUnselected"));
        listStyle.selection = toDrawable(mDropdownSelectionBackground);
        listStyle.background = toDrawable(mDropdownBackground);

        style.font = new BitmapFont();
        style.background = toDrawable(mBackground);
        style.backgroundDisabled = toDrawable(mBackgroundDisabled);
        style.backgroundOpen = toDrawable(mBackgroundOpen);
        style.backgroundOver = toDrawable(mBackgroundOver);
        style.fontColor = tableToColor(styleTable.get("textColor"));
        style.scrollStyle = new ScrollPane.ScrollPaneStyle();
        style.listStyle = listStyle;
    }

    @ExposeToLua
    public void setValueChangedListener(LuaFunction callback) {
        mChangedCallback = callback;
    }

    @ExposeToLua
    public void setItems(LuaTable table) {
        LuaArrayIterator luaArrayIterator = new LuaArrayIterator(table);
        disposeItems();
        while (luaArrayIterator.hasNext()) {
            LuaTable luaTable = (LuaTable)luaArrayIterator.next().arg(2);
            LuaValue value = luaTable.get("value");
            AssetID assetID = (AssetID)luaTable.get("text").checkuserdata(AssetID.class);
            mItems.add(new Item(value, assetID));
        }
        getActor().setItems(mItems);
    }

    @ExposeToLua
    public LuaValue getSelectedValue() {
        return getActor().getSelected().getValue();
    }

    @ExposeToLua
    public void setSelectedValue(LuaValue value) {
        for (Item item : mItems) {
            if (item.getValue().eq_b(value)) {
                getActor().setSelected(item);
                return;
            }
        }
        throw new LuaError("Entry with value: " + value.toString() + " not found");
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        mBackgroundDisabled = freeAsset(mBackgroundDisabled);
        mBackgroundOpen = freeAsset(mBackgroundOpen);
        mBackgroundOver = freeAsset(mBackgroundOver);
        mDropdownBackground = freeAsset(mDropdownBackground);
        mDropdownSelectionBackground = freeAsset(mDropdownSelectionBackground);
        disposeItems();
        super.dispose();
    }

    private void disposeItems() {
        for (Item item : mItems) {
            item.dispose();
        }
        mItems.clear();
    }

    public class Item implements Disposable {
        private ManagedAsset<String> mText;
        private LuaValue mValue;

        public Item(LuaValue value, AssetID assetID) {
            mValue = value;
            mText = swapAsset(AssetType.TEXT, assetID, mText, mAssetUpdatedObserver);
        }

        public LuaValue getValue() {
            return mValue;
        }

        @Override
        public void dispose() {
            mText = freeAsset(mText, mAssetUpdatedObserver);
        }

        @Override
        public String toString() {
            return mText.get();
        }
    }

    private class ChangeHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mChangedCallback != null) {
                mChangedCallback.call(DropDownFacade.this, getSelectedValue());
            }
        }
    }

    private class AssetUpdatedObserver implements AssetObserver<String> {
        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            getActor().invalidate();
        }
    }
}
