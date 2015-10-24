package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public abstract class ActorFacade<T extends Actor> extends ExposedJavaClass implements Disposable {
    protected final BaseScreen mScreen;
    protected T mActor;

    public ActorFacade(BaseScreen screen) {
        mScreen = screen;
    }

    protected void setActor(T actor) {
        mActor = actor;
    }

    public T getActor() {
        return mActor;
    }

    @Override
    public void dispose() {

    }
}
