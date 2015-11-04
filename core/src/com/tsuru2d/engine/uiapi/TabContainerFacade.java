package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private CallbackWrapper mFirstCallback;
    private ManagedAsset<Texture> mBackground;
    private AssetID mTabStyle;
    private Array<TextButtonFacade> mTabArray;

    public TabContainerFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
        mTabArray = new Array<TextButtonFacade>();
    }

    @Override
    public Table createActor(TabContainerStyle tabContainerStyle) {
        mTabs = new TableFacade(getScreen());
        mPanel = new TableFacade(getScreen());
        mTabs.initialize();
        mPanel.initialize();
        Table table = new Table();
        table.setDebug(true);
        mTabs.getActor().setDebug(true);
        mPanel.getActor().setDebug(true);
        table.add(mTabs.getActor()).left();
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

    @ExposeToLua
    public void replacePane(LuaValue table) {
        TableFacade newTable = (TableFacade) table;
        mPanel.getActor().clearChildren();
        mPanel.add(newTable).expand().fill();
    }

    @ExposeToLua
    public void addTab(AssetID text, LuaFunction callBack) {
    //public void addTab(String text, LuaFunction callBack) {
        TextButtonFacade tabFacade = new TextButtonFacade(getScreen(), mTabStyle);
        mTabArray.add(tabFacade);
        tabFacade.initialize();
        tabFacade.setText(text, null);
        CallbackWrapper callbackWrapper = new CallbackWrapper(callBack);
        tabFacade.setClickListener(callbackWrapper);
        mTabs.add(tabFacade).left();
        if(mFirstCallback == null) {
            mFirstCallback = callbackWrapper;
            mFirstCallback.call();
        }
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
}

