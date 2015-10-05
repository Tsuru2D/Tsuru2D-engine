package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;

/**
 * Created by zonglin on 10/4/15.
 */
public interface UIWrapper extends Disposable {

    Actor getActor();

}
