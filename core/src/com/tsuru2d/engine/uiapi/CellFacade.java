package com.tsuru2d.engine.uiapi;


import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public class CellFacade extends ExposedJavaClass {
    private final Cell<?> mCell;

    public CellFacade(Cell<?> cell) {
        mCell = cell;
    }

    /*********************************************** alignment ***************************************************/

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
    /*********************************************** size ***************************************************/

    @ExposeToLua
    public CellFacade size(double size) {
        mCell.size((float)size);
        return this;
    }


    @ExposeToLua
    public CellFacade size(double width,double height) {
        mCell.size((float)width,(float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade height(double height) {
        mCell.height((float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade minHeight(double height) {
        mCell.minHeight((float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade maxHeight(double height) {
        mCell.maxHeight((float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade prefHeight(double height) {
        mCell.prefHeight((float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade width(double height) {
        mCell.width((float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade minWidth(double height) {
        mCell.minHeight((float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade maxWidth(double height) {
        mCell.maxHeight((float)height);
        return this;
    }

    @ExposeToLua
    public CellFacade prefWidth(double height) {
        mCell.prefWidth((float)height);
        return this;
    }

    /*********************************************** padding *************************************************/

    @ExposeToLua
    public CellFacade pad(double pad) {
        mCell.pad((float)pad);
        return this;
    }

    @ExposeToLua
    public CellFacade pad (double top, double left, double bottom, double right) {
        mCell.pad((float) top, (float) left, (float) bottom, (float) right);
        return this;
    }

    @ExposeToLua
    public CellFacade padTop(double pad) {
        mCell.padTop((float)pad);
        return this;
    }

    @ExposeToLua
    public CellFacade padBottom(double pad) {
        mCell.padBottom((float)pad);
        return this;
    }

    @ExposeToLua
    public CellFacade padLeft(double pad) {
        mCell.padLeft((float)pad);
        return this;
    }

    @ExposeToLua
    public CellFacade padRight(double pad) {
        mCell.padRight((float)pad);
        return this;
    }

    /*********************************************** space *************************************************/

    @ExposeToLua
    public CellFacade space(double space) {
        mCell.space((float)space);
        return this;
    }

    @ExposeToLua
    public CellFacade space (double top, double left, double bottom, double right) {
        mCell.space((float)top, (float)left, (float)bottom, (float)right);
        return this;
    }

    @ExposeToLua
    public CellFacade spaceTop(double space) {
        mCell.spaceTop((float)space);
        return this;
    }

    @ExposeToLua
    public CellFacade spaceBottom(double space) {
        mCell.spaceBottom((float)space);
        return this;
    }

    @ExposeToLua
    public CellFacade spaceLeft(double space) {
        mCell.spaceLeft((float)space);
        return this;
    }

    @ExposeToLua
    public CellFacade spaceRight(double space) {
        mCell.spaceRight((float)space);
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
