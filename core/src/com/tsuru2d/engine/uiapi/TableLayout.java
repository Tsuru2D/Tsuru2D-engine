package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class TableLayout extends ClickListener implements UIWrapper {
    private BaseScreen mScreen;
    private LuaTable mLuaTable;
    private final Table mTable;
    private LuaFunction mCallBack;

    public TableLayout(BaseScreen screen, LuaTable data) {
        mTable = new Table();
        mLuaTable = data;
        mScreen = screen;
        mTable.setDebug(false);
    }

    public TableLayout(BaseScreen screen, LuaTable data, boolean debug) {
        mTable = new Table();
        mLuaTable = data;
        mScreen = screen;
        mTable.setDebug(debug);
    }

    @ExposeToLua
    public void add(UIWrapper wrapper) {
        mTable.add(wrapper.getActor());
    }

    @ExposeToLua
    public boolean remove(UIWrapper wrapper) {
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
    public Actor getActor() {
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
        Array<Cell> cells = mTable.getCells();
        for (Cell c : cells) {
            Disposable ui = (Disposable) c;
            ui.dispose();
        }
    }
}
