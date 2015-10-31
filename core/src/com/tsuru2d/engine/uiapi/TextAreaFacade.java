package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.tsuru2d.engine.gameapi.BaseScreen;

public class TextAreaFacade extends TextFieldFacade {
    public TextAreaFacade(BaseScreen baseScreen) {
        super(baseScreen);
    }

    @Override
    protected TextField createActor() {
        return new TextArea(null, createStyle());
    }

    @Override
    protected TextArea.TextFieldStyle createStyle() {
        TextArea.TextFieldStyle style = new TextArea.TextFieldStyle();
        style.font = new BitmapFont();
        return style;
    }
}
