package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class TableFacade extends ActorFacade<Table, Void> {
    public TableFacade(BaseScreen screen) {
        super(screen, null);
    }

    @Override
    protected Table createActor(Void style) {
        return new Table();
    }

    @Override
    protected Void createStyle() {
        return null;
    }

    @Override
    protected void populateStyle(Void aVoid, LuaTable styleTable) {

    }

    @ExposeToLua
    public CellFacade add(ActorFacade<? extends Actor, ?> wrapper) {
        Cell<? extends Actor> cell = getActor().add(wrapper.getActor());
        return new CellFacade(cell);
    }

    @ExposeToLua
    public boolean remove(ActorFacade<? extends Actor, ?> wrapper) {
        Actor actor = wrapper.getActor();
        return getActor().removeActor(actor);
    }

    @ExposeToLua
    public CellFacade row() {
        Cell cell = getActor().row();
        return new CellFacade(cell);
    }
}
