package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class TableLayoutFacade extends UIWrapper<Table> {
    private final Table mTable;


    public TableLayoutFacade(BaseScreen screen) {
        super(screen);
        mTable = new Table();
        mTable.setDebug(false);

    }

    public TableLayoutFacade(BaseScreen screen, LuaTable data, boolean debug) {
        super(screen);
        mTable = new Table();
        mTable.setDebug(debug);

    }

    @ExposeToLua
    public LuaValue add(Object wrapper) {
        UIWrapper uiWrapper = (UIWrapper)wrapper;
        Cell cell = mTable.add(uiWrapper.getActor());
        return new ExposedJavaClass(new CellWrapper(cell));
    }

    @ExposeToLua
    public boolean remove(Object wrapper) {     //do I need to wrap the boolean as a LuaValue?
        Actor actor = ((UIWrapper)wrapper).getActor();
        return mTable.removeActor(actor);
    }

    @ExposeToLua
    public LuaValue row() {
        Cell cell = mTable.row();
        return new ExposedJavaClass(new CellWrapper(cell));
    }

    /*
     * x is the ratio of the width of table to the width of the window.
     * x should be in the range [0,1]
     */
    @ExposeToLua
    public void setTableWidth(float ratio) {
        mTable.setWidth(mScreen.getWidth() * ratio);
    }

    @Override
    public void setPosition(float x, float y) {
        mTable.setPosition(x, y);
    }

    @Override
    public Table getActor() {
        return mTable;
    }

    @Override
    public void dispose() {
        //I don't know what should dispose() do
    }

    private class CellWrapper {   //Does it work? if it works, I can then add features to it.
        Cell mCell;

        private CellWrapper(Cell cell) {
            mCell = cell;
        }

        @ExposeToLua
        private LuaValue fillX() {
            return new ExposedJavaClass(this);
        }
    }
}
