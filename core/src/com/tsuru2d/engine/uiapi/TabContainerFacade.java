package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.util.HashMap;

public class TabContainerFacade extends ActorFacade<Table>{
    private TableFacade mTabs;
    private TableFacade mPanel;
    private LuaTable mTabStyle;

    public TabContainerFacade(BaseScreen screen) {
        super(screen);
        mTabs = new TableFacade(screen);
        mPanel = new TableFacade(screen);
        Table table = new Table();
        table.add(mTabs.getActor());
        table.row();
        table.add(mPanel.getActor());
        setActor(table);
    }

    @ExposeToLua
    public TableFacade getCleanPane() {
        Table panel = mPanel.getActor();
        for(Cell c : panel.getCells()) {
            panel.removeActor(c.getActor());
        }
        return mPanel;
    }

    @ExposeToLua
    public void setTabStyle(LuaTable tabStyle) {
        mTabStyle = tabStyle;
    }

    @ExposeToLua
    public void addTab(AssetID text, LuaFunction callBack) {
        TextButtonFacade tab = new TextButtonFacade(mScreen);
        tab.setText(text);
        tab.setOnClick(callBack);
        mTabs.add(tab);
    }

}
