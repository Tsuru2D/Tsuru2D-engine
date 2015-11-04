package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public class LuaUIManager extends ExposedJavaClass implements Disposable {
    private final BaseScreen mScreen;
    private final Table mTable;
    private final Array<ActorFacade<?, ?>> mControls;

    public LuaUIManager(BaseScreen screen, Table rootTable) {
        mScreen = screen;
        mTable = rootTable;
        mControls = new Array<ActorFacade<?, ?>>();
    }

    private void initActor(ActorFacade<?, ?> actor) {
        actor.initialize();
        mControls.add(actor);
    }

    @ExposeToLua
    public CellFacade add(ActorFacade<?, ?> actor) {
        Cell<?> cell = mTable.add(actor.getActor());
        return new CellFacade(cell);
    }

    @ExposeToLua
    public void remove(ActorFacade<?, ?> actor) {
        mTable.removeActor(actor.getActor());
    }

    @ExposeToLua
    public CellFacade row() {
        Cell cell = mTable.row();
        return new CellFacade(cell);
    }

    @ExposeToLua
    public TableFacade newTable() {
        TableFacade table = new TableFacade(mScreen);
        initActor(table);
        return table;
    }

    @ExposeToLua
    public TextButtonFacade newTextButton(AssetID styleID) {
        TextButtonFacade button = new TextButtonFacade(mScreen, styleID);
        initActor(button);
        return button;
    }

    @ExposeToLua
    public ButtonFacade newButton(AssetID styleID) {
        ButtonFacade button = new ButtonFacade(mScreen, styleID);
        initActor(button);
        return button;
    }

    @ExposeToLua
    public LabelFacade newLabel(AssetID styleID) {
        LabelFacade label = new LabelFacade(mScreen, styleID);
        initActor(label);
        return label;
    }

    @ExposeToLua
    public TextFieldFacade newTextField(AssetID styleID) {
        TextFieldFacade textField = new TextFieldFacade(mScreen, styleID);
        initActor(textField);
        return textField;
    }

    @ExposeToLua
    public TextAreaFacade newTextArea(AssetID styleID) {
        TextAreaFacade textArea = new TextAreaFacade(mScreen, styleID);
        initActor(textArea);
        return textArea;
    }

    @ExposeToLua
    public CheckBoxFacade newCheckBox(AssetID styleID) {
        CheckBoxFacade checkBox = new CheckBoxFacade(mScreen, styleID);
        initActor(checkBox);
        return checkBox;
    }

    @ExposeToLua
    public SliderFacade newSlider(AssetID styleID) {
        SliderFacade slider = new SliderFacade(mScreen, styleID);
        initActor(slider);
        return slider;
    }

    @ExposeToLua
    public ScrollPaneFacade newScrollPane(AssetID styleID) {
        ScrollPaneFacade scrollPane = new ScrollPaneFacade(mScreen, styleID);
        initActor(scrollPane);
        return scrollPane;
    }
    @Override
    public void dispose() {
        for (ActorFacade<?, ?> control : mControls) {
            control.dispose();
        }
    }
}
