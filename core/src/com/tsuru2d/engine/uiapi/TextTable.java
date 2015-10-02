package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.tsuru2d.engine.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetObserver;
import com.tsuru2d.engine.loader.ManagedAsset;
import org.luaj.vm2.LuaTable;

public class TextTable {
    private BaseScreen mScreen;
    private Table table;
    private ManagedAsset<String>[][] tableEntries;
    private AssetObserver<String>[][] observers;

    /**
     * Create a TextTable with debug disabled
     * @param screen
     * @param data
     */
    public TextTable(BaseScreen screen, LuaTable data){
        mScreen = screen;
        table = new Table();
        table.setDebug(false);
    }

    /**
     * Create a TextTable with debug enabled (will show the grid of the table)
     * @param screen
     * @param data
     * @param debug
     */
    public TextTable(BaseScreen screen, LuaTable data, boolean debug){
        mScreen = screen;
        table = new Table();
        table.setDebug(debug);
    }

    /**
     * Set the text for each cell of the table. There is no Skin specified, so the default will be used
     * @param tableAssets
     */
    public void setTextcontent(AssetID[][] tableAssets) {
        setTextcontentwithSkin(tableAssets, new Skin());
    }

    /**
     * Set the width for <b>each</b> table cell.
     * @param width
     */
    public void setCellWidth(int width) {
        table.defaults().width(width);
    }

    /**
     * Set the text for each cell of the table with the specified Skin.
     * @param tableAssets
     * @param skin
     */
    @SuppressWarnings("unchecked")
    public void setTextcontentwithSkin(AssetID[][] tableAssets, Skin skin) {
        dispose();
        int rows = tableAssets.length, cols = tableAssets[0].length;
        tableEntries = new ManagedAsset[rows][cols];
        observers = new AssetObserver[rows][cols];
        for(int i = 0; i < rows - 1; i++){
            for(int j = 0; j < cols; j++){
                Label label = new Label("", skin);
                observers[i][j] = new TextObserver(label);
                tableEntries[i][j] = mScreen.getAssetLoader().getText(tableAssets[i][j]);
            }
            table.row();
        }
        rows --;
        for(int j = 0; j < cols; j++){
            Label label = new Label("", new Label.LabelStyle());
            observers[rows][j] = new TextObserver(label);
            tableEntries[rows][j] = mScreen.getAssetLoader().getText(tableAssets[rows][j]);
        }
    }

    public void dispose() {
        for(int i = 0; i < tableEntries.length; i++){
            for(int j = 0; j < tableEntries[i].length; j++){
                if(tableEntries[i][j] != null && observers[i][j] != null){
                    tableEntries[i][j].removeObserver(observers[i][j]);
                    mScreen.getAssetLoader().freeAsset(tableEntries[i][j]);
                }
            }
        }
    }

    private class TextObserver implements AssetObserver<String> {

        private Label owner;

        public TextObserver(Label owner) {
            this.owner = owner;
        }

        @Override
        public void onAssetUpdated(ManagedAsset<String> asset) {
            owner.setText(asset.get());
        }
    }
}
