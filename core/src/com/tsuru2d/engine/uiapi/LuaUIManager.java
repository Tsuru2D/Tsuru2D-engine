package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public class LuaUIManager extends ExposedJavaClass implements Disposable {
    private final BaseScreen mScreen;
    private final Table mTable;

    public LuaUIManager(BaseScreen screen, Table rootTable) {
        mScreen = screen;
        mTable = rootTable;
    }

    @ExposeToLua
    public CellFacade add(ActorFacade<?> actor) {
        Cell<?> cell = mTable.add(actor.getActor());
        return new CellFacade(cell);
    }

    @ExposeToLua
    public TableFacade newTable() {
        return new TableFacade(mScreen);
    }

    @ExposeToLua
    public TextButtonFacade newTextButton() {
        return new TextButtonFacade(mScreen);
    }

    @ExposeToLua
    public ButtonFacade newButton() {
        return new ButtonFacade(mScreen);
    }

    @ExposeToLua
    public LabelFacade newLabel() {
        return new LabelFacade(mScreen);
    }

    @Override
    public void dispose() {
        // TODO
    }
}
