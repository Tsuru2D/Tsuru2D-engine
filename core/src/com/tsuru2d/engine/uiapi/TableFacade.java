package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;

public class TableFacade extends ActorFacade<Table> {
    public TableFacade(BaseScreen screen) {
        super(screen, new Table());
    }

    @ExposeToLua
    public CellFacade add(ActorFacade<? extends Actor> wrapper) {
        Cell<? extends Actor> cell = mActor.add(wrapper.mActor);
        return new CellFacade(cell);
    }

    @ExposeToLua
    public boolean remove(ActorFacade<? extends Actor> wrapper) {
        Actor actor = wrapper.mActor;
        return mActor.removeActor(actor);
    }

    @ExposeToLua
    public CellFacade row() {
        Cell cell = mActor.row();
        return new CellFacade(cell);
    }
}