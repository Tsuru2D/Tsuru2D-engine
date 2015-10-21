package com.tsuru2d.engine.uiapi;


import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public class CellFacade extends ExposedJavaClass {
    private final Cell<?> mCell;

    public CellFacade(Cell<?> cell) {
        mCell = cell;
    }

    @ExposeToLua
    public CellFacade fillX() {
        mCell.fillX();
        return this;
    }

    @ExposeToLua
    public CellFacade fillY() {
        mCell.fillY();
        return this;
    }
}
