package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

@Deprecated
public class FontFactory {
    public static BitmapFont font24() {
        return new BitmapFont(Gdx.files.internal("segoe24.fnt"));
    }
}
