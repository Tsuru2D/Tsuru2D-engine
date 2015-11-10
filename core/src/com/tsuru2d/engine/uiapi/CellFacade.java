package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.Varargs;

public class CellFacade extends ExposedJavaClass {
    private final Cell<?> mCell;

    public CellFacade(Cell<?> cell) {
        mCell = cell;
    }

    @ExposeToLua
    public CellFacade fill() {
        mCell.fill();
        return this;
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

    @ExposeToLua
    public CellFacade expand() {
        mCell.expand();
        return this;
    }

    @ExposeToLua
    public CellFacade expandX() {
        mCell.expandX();
        return this;
    }

    @ExposeToLua
    public CellFacade expandY() {
        mCell.expandY();
        return this;
    }

    @ExposeToLua
    public CellFacade top() {
        mCell.top();
        return this;
    }

    @ExposeToLua
    public CellFacade bottom() {
        mCell.bottom();
        return this;
    }

    @ExposeToLua
    public CellFacade right() {
        mCell.right();
        return this;
    }

    @ExposeToLua
    public CellFacade left() {
        mCell.left();
        return this;
    }

    @ExposeToLua
    public CellFacade size(Varargs args) {
        if (args.narg() == 1) {
            mCell.size((float)args.arg1().checkdouble());
        } else if (args.narg() == 2) {
            mCell.size(
                (float)args.arg(1).checkdouble(),
                (float)args.arg(2).checkdouble());
        } else {
            throw new LuaError("Invalid argument count passed to size()");
        }
        return this;
    }

    @ExposeToLua
    public CellFacade height(float height) {
        mCell.height(height);
        return this;
    }

    @ExposeToLua
    public CellFacade minHeight(float height) {
        mCell.minHeight(height);
        return this;
    }

    @ExposeToLua
    public CellFacade maxHeight(float height) {
        mCell.maxHeight(height);
        return this;
    }

    @ExposeToLua
    public CellFacade prefHeight(float height) {
        mCell.prefHeight(height);
        return this;
    }

    @ExposeToLua
    public CellFacade width(float width) {
        mCell.width(width);
        return this;
    }

    @ExposeToLua
    public CellFacade minWidth(float width) {
        mCell.minWidth(width);
        return this;
    }

    @ExposeToLua
    public CellFacade maxWidth(float width) {
        mCell.maxWidth(width);
        return this;
    }

    @ExposeToLua
    public CellFacade prefWidth(float width) {
        mCell.prefWidth(width);
        return this;
    }

    @ExposeToLua
    public CellFacade pad(Varargs args) {
        if (args.narg() == 1) {
            mCell.pad((float)args.arg1().checkdouble());
        } else if (args.narg() == 4) {
            mCell.pad(
                (float)args.arg(1).checkdouble(),
                (float)args.arg(2).checkdouble(),
                (float)args.arg(3).checkdouble(),
                (float)args.arg(4).checkdouble());
        } else {
            throw new LuaError("Invalid argument count passed to pad()");
        }
        return this;
    }

    @ExposeToLua
    public CellFacade padTop(float pad) {
        mCell.padTop(pad);
        return this;
    }

    @ExposeToLua
    public CellFacade padBottom(float pad) {
        mCell.padBottom(pad);
        return this;
    }

    @ExposeToLua
    public CellFacade padLeft(float pad) {
        mCell.padLeft(pad);
        return this;
    }

    @ExposeToLua
    public CellFacade padRight(float pad) {
        mCell.padRight(pad);
        return this;
    }

    @ExposeToLua
    public CellFacade space(Varargs args) {
        if (args.narg() == 1) {
            mCell.space((float)args.arg1().checkdouble());
        } else if (args.narg() == 4) {
            mCell.space(
                (float)args.arg(1).checkdouble(),
                (float)args.arg(2).checkdouble(),
                (float)args.arg(3).checkdouble(),
                (float)args.arg(4).checkdouble());
        } else {
            throw new LuaError("Invalid argument count passed to space()");
        }
        return this;
    }

    @ExposeToLua
    public CellFacade spaceTop(float space) {
        mCell.spaceTop(space);
        return this;
    }

    @ExposeToLua
    public CellFacade spaceBottom(float space) {
        mCell.spaceBottom(space);
        return this;
    }

    @ExposeToLua
    public CellFacade spaceLeft(float space) {
        mCell.spaceLeft(space);
        return this;
    }

    @ExposeToLua
    public CellFacade spaceRight(float space) {
        mCell.spaceRight(space);
        return this;
    }

    @ExposeToLua
    public CellFacade uniform() {
        mCell.uniform();
        return this;
    }

    @ExposeToLua
    public CellFacade colspan(int col) {
        mCell.colspan(col);
        return this;
    }
}
