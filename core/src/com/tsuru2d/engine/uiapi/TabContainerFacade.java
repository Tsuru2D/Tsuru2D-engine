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

public class TabContainerFacade extends ActorFacade<Table, TabContainerFacade.TabContainerStyle>{
    private TableFacade mTabs;
    private TableFacade mPanel;
    private ScrollPane mScrollPane;
    private ButtonGroup mButtonGroup;
    private ManagedAsset<Texture> mBackground;
    private AssetID mTabStyle;
    private Array<ActorFacade> mTabArray;

    public TabContainerFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
        mTabArray = new Array<ActorFacade>();
    }

    @Override
    public Table createActor(TabContainerStyle tabContainerStyle) {
        mTabs = new TableFacade(getScreen());
        mPanel = new TableFacade(getScreen());
        mTabs.initialize();
        mPanel.initialize();
        mTabs.getActor().setDebug(false);
        mPanel.getActor().setDebug(false);
        Table table = new Table();
        mScrollPane = new ScrollPane(mTabs.getActor());
        mButtonGroup = new ButtonGroup();
        mButtonGroup.setMaxCheckCount(1);
        mButtonGroup.setMinCheckCount(1);
        table.add(mScrollPane).left();
        table.row();
        table.add(mPanel.getActor()).expand().fill();
        table.setBackground(tabContainerStyle.background);
        return table;
    }

    @Override
    protected TabContainerStyle createStyle() {
        TabContainerStyle style = new TabContainerStyle();
        return style;
    }

    @Override
    protected void populateStyle(TabContainerStyle style, LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, "background", mBackground);
        mTabStyle = (AssetID)styleTable.get("buttonStyle").checkuserdata(AssetID.class);
        style.background = toDrawable(mBackground);
    }

    public void replacePane(LuaValue table) {
        TableFacade newTable = (TableFacade) table;
        newTable.getActor().setDebug(false);
        mPanel.getActor().clearChildren();
        mPanel.add(newTable).expand().fill();
    }

    @ExposeToLua
    public void addTab(AssetID text, LuaFunction callBack) {
        Tab tab = new Tab(getScreen(), mTabStyle);
        tab.initialize();
        tab.setText(text, null);
        CallbackWrapper callbackWrapper = new CallbackWrapper(callBack);
        tab.setClickListener(callbackWrapper);
        mTabs.add(tab).left();
        mButtonGroup.add(tab.getActor());
        mTabs.getActor().setHeight(tab.getActor().getPrefHeight());
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        for(int i = 0; i < mTabArray.size; i++) {
            mTabArray.get(i).dispose();
        }
        mTabs.dispose();
        mPanel.dispose();
        super.dispose();
    }

    public class TabContainerStyle {
        /**Optional*/
        public Drawable background;
        /**Optional*/
        public AssetID tabStyle;
        public TabContainerStyle() {
        }
    }

    private class CallbackWrapper extends ZeroArgFunction {

        private LuaFunction mCallback;
        public CallbackWrapper(LuaFunction function) {
            mCallback = function;
        }
        @Override
        public LuaValue call() {
            LuaValue table = mCallback.call();
            replacePane(table);
            return LuaValue.NIL;
        }
    }

    private class Tab extends TextButtonFacade {
        private ManagedAsset<Texture> mChecked;

        public Tab(BaseScreen screen, AssetID styleID) {
            super(screen, styleID);
        }

        /*
         * will use the "down" as checked texture
         */
        @Override
        protected void populateStyle(Button.ButtonStyle style, LuaTable styleTable) {
            super.populateStyle(style, styleTable);
            mChecked = swapStyleImage(styleTable, "down", mChecked);
            TextButton.TextButtonStyle s = (TextButton.TextButtonStyle)style;
            s.checked = toDrawable(mChecked);
        }

        @Override
        public void dispose() {
            mChecked = freeAsset(mChecked);
            super.dispose();
        }
    }
}

