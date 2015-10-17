package com.tsuru2d.engine.gameapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class GameActor extends Actor {
    private Texture mTexture;

    public void setTexture(Texture texture) {
        mTexture=texture;
    }

    @Override
    public void draw (Batch batch, float parentAlpha) {
        batch.draw(
            mTexture,getX(),getY(),getOriginX(),getOriginY(),getHeight(),getWidth(),getScaleX(),getScaleY(),getRotation(),0,0,mTexture.getWidth(),mTexture.getHeight(),false,false);
    }
}
