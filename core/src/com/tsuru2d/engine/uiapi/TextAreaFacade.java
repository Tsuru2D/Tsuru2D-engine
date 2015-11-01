package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;

public class TextAreaFacade extends TextFieldFacade {
    public TextAreaFacade(BaseScreen baseScreen, AssetID styleID) {
        super(baseScreen, styleID);
    }

    @Override
    protected TextArea createActor(TextField.TextFieldStyle style) {
        return new TextArea(null, style);
    }
}
