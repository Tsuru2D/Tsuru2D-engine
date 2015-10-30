package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.ManagedAsset;
import org.luaj.vm2.LuaFunction;

public class ListFacade extends ActorFacade<List>{
    private ManagedAsset<Texture> mBackground,mSelection;
    private LuaFunction mCallBack;

    public ListFacade(BaseScreen screen){
        super(screen);
        List list=new List(createStyle());
    }

    public List.ListStyle createStyle(){
        List.ListStyle listStyle=new List.ListStyle(new BitmapFont(),Color.BLACK,Color.BLUE,getDrawable(mSelection));
        listStyle.background=getDrawable(mBackground);
        return listStyle;
    }

    private TextureRegionDrawable getDrawable(ManagedAsset<Texture> texture) {
        return (texture==null)? null:new TextureRegionDrawable(new TextureRegion(texture.get()));
    }

}
