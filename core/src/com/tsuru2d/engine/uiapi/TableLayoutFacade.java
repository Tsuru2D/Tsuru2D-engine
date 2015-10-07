package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

import java.util.HashMap;

public class TableLayoutFacade extends ClickListener implements UIWrapper<Table> {
    private BaseScreen mScreen;
    private final Table mTable;
    private LuaFunction mCallBack;
    private HashMap<Integer, UIWrapper> mMap;

    public TableLayoutFacade(BaseScreen screen, LuaTable data) {
        mTable = new Table();
        mScreen = screen;
        mTable.setDebug(false);
        mMap = new HashMap<>();
    }

    public TableLayoutFacade(BaseScreen screen, LuaTable data, boolean debug) {
        mTable = new Table();
        mScreen = screen;
        mTable.setDebug(debug);
    }

    @ExposeToLua
    public void add(UIWrapper<?> wrapper) {
        mMap.put(wrapper.hashCode(), wrapper);
        mTable.add(wrapper.getActor());
    }

    @ExposeToLua
    public boolean remove(UIWrapper wrapper) {
        mMap.remove(wrapper.hashCode());
        return mTable.removeActor(wrapper.getActor());
    }

    @ExposeToLua
    public void row() {
        mTable.row();
    }

    /*
     * x is the ratio of the width of table to the width of the window.
     * x should be in the range [0,1]
     */
    @ExposeToLua
    public void setTableWidth(float ratio) {
        mTable.setWidth(mScreen.getWidth() * ratio);
    }

    @ExposeToLua
    public void setOnClick(LuaFunction callBack) {
        mCallBack = callBack;
        if (mCallBack != null) {
            mTable.addListener(this);
        } else {
            mTable.removeListener(this);
        }
    }

    @Override
    public Table getActor() {
        return mTable;
    }

    @Override
    public void clicked(InputEvent event, float x, float y) {
        if (mCallBack != null) {
            mCallBack.call();
        }
    }

    @Override
    public void dispose() {
        Object keys[] = mMap.keySet().toArray();
        for(int i = 0; i < keys.length; i++) {
            mMap.get(keys[i]).dispose();
        }
    }
}
