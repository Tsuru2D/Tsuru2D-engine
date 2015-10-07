package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

public interface UIWrapper<T extends Actor> extends Disposable {

    T getActor();

}
