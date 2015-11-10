package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.ZeroArgFunction;

public class TabContainerFacade extends ActorFacade<Table, TabContainerFacade.TabContainerStyle> {
    private TableFacade mTabs;
    private TableFacade mPanel;
    private ScrollPane mScrollPane;
    private ButtonGroup<TextButton> mButtonGroup;
    private ManagedAsset<Texture> mBackground;
    private TabContainerStyle mTabContainerStyle;
    private Array<Tab> mTabArray;

    public TabContainerFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
        mTabArray = new Array<Tab>();
    }

    @Override
    public Table createActor(TabContainerStyle tabContainerStyle) {
        mTabs = new TableFacade(getScreen());
        mPanel = new TableFacade(getScreen());
        mTabs.initialize();
        mPanel.initialize();
        Table table = new Table();
        mScrollPane = new ScrollPane(mTabs.getActor());
        mButtonGroup = new ButtonGroup<TextButton>();
        mButtonGroup.setMaxCheckCount(1);
        mButtonGroup.setMinCheckCount(1);
        table.add(mScrollPane).left();
        table.row();
        table.add(mPanel.getActor()).expand().fill();
        table.setBackground(tabContainerStyle.background);
        mTabContainerStyle = tabContainerStyle;
        return table;
    }

    @Override
    protected TabContainerStyle createStyle() {
        return new TabContainerStyle();
    }

    @Override
    protected void populateStyle(TabContainerStyle style, LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, BACKGROUND, mBackground);
        style.tabStyle = (AssetID)styleTable.get(TAB_STYLE).checkuserdata(AssetID.class);
        style.background = toDrawable(mBackground);
    }

    private void replacePane(LuaValue table) {
        TableFacade newTable = (TableFacade)table;
        mPanel.getActor().clearChildren();
        mPanel.add(newTable).expand().fill();
    }

    @ExposeToLua
    public void addTab(AssetID text, LuaFunction callBack) {
        Tab tab = new Tab(getScreen(), mTabContainerStyle.tabStyle);
        tab.initialize();
        tab.setText(text, null);
        CallbackWrapper callbackWrapper = new CallbackWrapper(callBack);
        tab.setClickListener(callbackWrapper);
        mTabs.add(tab).left();
        mButtonGroup.add(tab.getActor());
        mTabArray.add(tab);
        mTabs.getActor().setHeight(tab.getActor().getPrefHeight());
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        for (int i = 0; i < mTabArray.size; i++) {
            mTabArray.get(i).dispose();
        }
        mTabs.dispose();
        mPanel.dispose();
        super.dispose();
    }

    public static class TabContainerStyle {
        /**
         * Optional
         */
        public Drawable background;
        /**
         * Required
         */
        public AssetID tabStyle;
    }

    private class CallbackWrapper extends ZeroArgFunction {
        private LuaFunction mCallback;

        public CallbackWrapper(LuaFunction callback) {
            mCallback = callback;
        }

        @Override
        public LuaValue call() {
            LuaValue table = mCallback.call();
            replacePane(table);
            return LuaValue.NIL;
        }
    }

    private static class Tab extends TextButtonFacade {
        public Tab(BaseScreen screen, AssetID styleID) {
            super(screen, styleID);
        }
    }
}

