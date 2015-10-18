package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import com.tsuru2d.engine.lua.LuaMapIterator;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import java.util.Iterator;

public class GameActor extends ExposedJavaClass {
    private Texture mTexture;
    private Actor mActor;

    public void setTexture(Texture texture) {
        mTexture = texture;
    }

    public void transform(LuaTable transformation) {
        Iterator<Varargs> it = new LuaMapIterator(transformation);
        while (it.hasNext()) {
            Varargs transformKvp = it.next();
            String key = transformKvp.arg(1).checkjstring();
            LuaValue rawValue = transformKvp.arg(2);
            if (key.equals("x")) {
                MoveToAction action = Actions.action(MoveToAction.class);
                action.setX((float)rawValue.checkdouble());
                mActor.addAction(action);
            } else if (key.equals("y")) {
                MoveToAction action = Actions.action(MoveToAction.class);
                action.setY((float)rawValue.checkdouble());
                mActor.addAction(action);
            }
        }
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(
            mTexture,
            mActor.getX(),
            mActor.getY(),
            mActor.getOriginX(),
            mActor.getOriginY(),
            mActor.getHeight(),
            mActor.getWidth(),
            mActor.getScaleX(),
            mActor.getScaleY(),
            mActor.getRotation(),
            0,
            0,
            mTexture.getWidth(),
            mTexture.getHeight(),
            false,
            false);
    }
}
