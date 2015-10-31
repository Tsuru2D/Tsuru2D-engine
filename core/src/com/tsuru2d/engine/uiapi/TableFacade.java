package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;

public class TableFacade extends ActorFacade<Table> {
    public TableFacade(BaseScreen screen) {
        super(screen);
        setActor(new Table());
    }

    @ExposeToLua
    public CellFacade add(ActorFacade<? extends Actor> wrapper) {
        Cell<? extends Actor> cell = getActor().add(wrapper.getActor());
        return new CellFacade(cell);
    }

    @ExposeToLua
    public boolean remove(ActorFacade<? extends Actor> wrapper) {
        Actor actor = wrapper.getActor();
        return getActor().removeActor(actor);
    }

    @ExposeToLua
    public CellFacade row() {
        Cell cell = getActor().row();
        return new CellFacade(cell);
    }
}
