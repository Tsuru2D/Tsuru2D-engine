package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;

public class DropDownFacade extends ActorFacade<SelectBox>{
    private ManagedAsset<Texture> mBackground,mSelection,
        mBackgroundDisabled,mBackgroundOpen,mBackgroundOver;
    private LuaFunction mCallBack;
    private ChangeHandler mChangeHandler;

    public DropDownFacade(BaseScreen screen){
        super(screen);
        SelectBox selectBox=new SelectBox(createStyle());
        setActor(selectBox);
        mChangeHandler = new ChangeHandler();
    }

    public SelectBox.SelectBoxStyle createStyle(){
        BitmapFont font=new BitmapFont();
        TextureRegionDrawable backGround=getDrawable(mBackground);
        List.ListStyle listStyle=new List.ListStyle
            (font,Color.BLACK,Color.BLUE,getDrawable(mSelection));
        listStyle.background=backGround; // can be optional out if error occurs
        SelectBox.SelectBoxStyle selectBoxStyle=new SelectBox.SelectBoxStyle
            (font,Color.BLUE,backGround,new ScrollPane.ScrollPaneStyle(),listStyle);
        selectBoxStyle.backgroundDisabled=getDrawable(mBackgroundDisabled);
        selectBoxStyle.backgroundOpen=getDrawable(mBackgroundOpen);
        selectBoxStyle.backgroundOver=getDrawable(mBackgroundOver);
        selectBoxStyle.disabledFontColor=Color.GOLD;
        return selectBoxStyle;
    }

    private TextureRegionDrawable getDrawable(ManagedAsset<Texture> texture) {
        return (texture==null)? null:new TextureRegionDrawable(new TextureRegion(texture.get()));
    }

    @ExposeToLua
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
    }

    @ExposeToLua
    public void setOnChange(LuaFunction callBack) {
        mCallBack = callBack;
        if(mCallBack != null) {
            getActor().addListener(mChangeHandler);
        }else {
            getActor().removeListener(mChangeHandler);
        }
    }

    @Override
    public void dispose() {
        dispose(mBackground);
        dispose(mSelection);
        dispose(mBackgroundDisabled);
        dispose(mBackgroundOpen);
        dispose(mBackgroundOver);
    }

    private void dispose(ManagedAsset asset){
        if(asset!=null) {
            mScreen.getAssetLoader().freeAsset(asset);
            asset=null;
        }
    }

    private class ChangeHandler extends ChangeListener {
        @Override
        public void changed(ChangeEvent event, Actor actor) {
            getActor().setSelectedIndex(getActor().getSelectedIndex());
            if(mCallBack != null) {
                mCallBack.call();
            }
        }
    }

}
