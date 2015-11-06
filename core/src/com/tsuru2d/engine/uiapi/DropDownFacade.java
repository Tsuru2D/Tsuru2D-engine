package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class DropDownFacade extends ActorFacade<SelectBox, SelectBox.SelectBoxStyle>{
    private ManagedAsset<Texture> mBackground,mSelection,
        mBackgroundDisabled,mBackgroundOpen,mBackgroundOver;
    private LuaFunction mCallBack;
    private LuaFunction mChangedCallback;

    public DropDownFacade(BaseScreen screen,AssetID styleID){
        super(screen,styleID);
    }

    @Override
    protected SelectBox createActor(SelectBox.SelectBoxStyle style) {
        return new SelectBox(style);
    }

    @Override
    protected void initializeActor(SelectBox actor) {
        actor.addListener(new ChangeHandler());
    }

    public SelectBox.SelectBoxStyle createStyle(){
        /*BitmapFont font=new BitmapFont();
        TextureRegionDrawable backGround=toDrawable(mBackground);
        List.ListStyle listStyle=new List.ListStyle
            (font,Color.BLACK,Color.BLUE,toDrawable(mSelection););
        listStyle.background=backGround; // can be optional out if error occurs
        SelectBox.SelectBoxStyle selectBoxStyle=new SelectBox.SelectBoxStyle
            (font,Color.BLUE,backGround,new ScrollPane.ScrollPaneStyle(),listStyle);
        selectBoxStyle.backgroundDisabled=getDrawable(mBackgroundDisabled);
        selectBoxStyle.backgroundOpen=getDrawable(mBackgroundOpen);
        selectBoxStyle.backgroundOver=getDrawable(mBackgroundOver);
        selectBoxStyle.disabledFontColor=Color.GOLD;
        return selectBoxStyle;*/
        return new SelectBox.SelectBoxStyle();
    }

    /*private TextureRegionDrawable getDrawable(ManagedAsset<Texture> texture) {
        return (texture==null)? null:new TextureRegionDrawable(new TextureRegion(texture.get()));
    }*/

    @Override
    protected void populateStyle(SelectBox.SelectBoxStyle style, LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, "background", mBackground);
        mSelection = swapStyleImage(styleTable, "selection", mSelection);
        mBackgroundDisabled = swapStyleImage(styleTable, "backgroundDisabled", mBackgroundDisabled);
        mBackgroundOpen = swapStyleImage(styleTable, "backgroundOpen", mBackgroundOpen);
        mBackgroundOver = swapStyleImage(styleTable, "backgroundOver", mBackgroundOver);

        BitmapFont font=new BitmapFont();
        List.ListStyle listStyle=new List.ListStyle
            (font,Color.BLACK,Color.BLUE,toDrawable(mSelection));
        listStyle.background=toDrawable(mBackground);

        style.font=font;
        style.background = toDrawable(mBackground);
        style.backgroundDisabled = toDrawable(mBackgroundDisabled);
        style.backgroundOpen = toDrawable(mBackgroundOpen);
        style.backgroundOver = toDrawable(mBackgroundOver);
        style.fontColor = tableToColor(styleTable.get("textColor"));
        style.scrollStyle=new ScrollPane.ScrollPaneStyle();
        style.listStyle=listStyle;
    }

    @ExposeToLua
    public void setChangedListener(LuaFunction callback) {
        mChangedCallback = callback;
    }

    /*@ExposeToLua
    public void setStyle(LuaTable styleTable) {
        dispose();
        mBackground = getStyleAsset(styleTable, "background");
        mSelection = getStyleAsset(styleTable, "selection");
        mBackgroundDisabled = getStyleAsset(styleTable, "backgrounddisabled");
        mBackgroundOpen = getStyleAsset(styleTable, "backgroundopen");
        mBackgroundOver = getStyleAsset(styleTable, "backgroundover");
        mBackground = getStyleAsset(styleTable, "background");
        SelectBox.SelectBoxStyle selectBoxStyle = createStyle();
        mActor.setStyle(selectBoxStyle);
    }*/

    @ExposeToLua
    public void setItems(LuaTable table){
        int k=1;
        Array<String> items=new Array<String>();
        while(table.get(k)!=null){
            AssetID assetID = (AssetID)table.get(k).checkuserdata(AssetID.class);
            items.add((String)(getAsset(AssetType.TEXT, assetID).get()));
            k++;
        }
        getActor().setItems(items);
    }

    /*@ExposeToLua
    public void setOnChange(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            getActor().addListener();
        }else {
            getActor().removeListener();
        }
    }*/

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        mSelection = freeAsset(mSelection);
        mBackgroundDisabled = freeAsset(mBackgroundDisabled);
        mBackgroundOpen = freeAsset(mBackgroundOpen);
        mBackgroundOver = freeAsset(mBackgroundOver);
    }

    /*private void dispose(ManagedAsset asset){
        if(asset!=null) {
            mScreen.getAssetLoader().freeAsset(asset);
            asset=null;
        }
    }*/

    private class ChangeHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            if (mChangedCallback != null) {
                mChangedCallback.call(DropDownFacade.this, LuaValue.valueOf("13190"));
            }
        }
    }

}
