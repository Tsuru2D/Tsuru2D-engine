package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public class GameActor extends ExposedJavaClass {
    private Texture mTexture;
    private Actor mActor;

    public void setTexture(Texture texture) {
        mTexture = texture;
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
