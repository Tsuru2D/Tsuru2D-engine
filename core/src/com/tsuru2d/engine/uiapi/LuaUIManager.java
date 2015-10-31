package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.lua.ExposeToLua;
import com.tsuru2d.engine.lua.ExposedJavaClass;

public class LuaUIManager extends ExposedJavaClass implements Disposable {
    private final BaseScreen mScreen;
    private final Table mTable;
    private final Array<ActorFacade<?>> mControls;

    public LuaUIManager(BaseScreen screen, Table rootTable) {
        mScreen = screen;
        mTable = rootTable;
        mControls = new Array<ActorFacade<?>>();
    }

    @ExposeToLua
    public CellFacade add(ActorFacade<?> actor) {
        Cell<?> cell = mTable.add(actor.getActor());
        return new CellFacade(cell);
    }

    @ExposeToLua
    public void remove(ActorFacade<?> actor) {
        mTable.removeActor(actor.getActor());
    }

    @ExposeToLua
    public TableFacade newTable() {
        TableFacade table = new TableFacade(mScreen);
        mControls.add(table);
        return table;
    }

    @ExposeToLua
    public TextButtonFacade newTextButton() {
        TextButtonFacade button = new TextButtonFacade(mScreen);
        mControls.add(button);
        return button;
    }

    @ExposeToLua
    public ButtonFacade newButton() {
        ButtonFacade button = new ButtonFacade(mScreen);
        mControls.add(button);
        return button;
    }

    @ExposeToLua
    public LabelFacade newLabel() {
        LabelFacade label = new LabelFacade(mScreen);
        mControls.add(label);
        return label;
    }

    @ExposeToLua
    public TextFieldFacade newTextField() {
        TextFieldFacade textField = new TextFieldFacade(mScreen);
        mControls.add(textField);
        return textField;
    }

    @ExposeToLua
    public CheckBoxFacade newCheckBox() {
        CheckBoxFacade checkBox = new CheckBoxFacade(mScreen);
        mControls.add(checkBox);
        return checkBox;
    }

    @ExposeToLua
    public SliderFacade newSlider() {
        SliderFacade slider = new SliderFacade(mScreen);
        mControls.add(slider);
        return slider;
    }

    @Override
    public void dispose() {
        for (ActorFacade<?> control : mControls) {
            control.dispose();
        }
    }
}
