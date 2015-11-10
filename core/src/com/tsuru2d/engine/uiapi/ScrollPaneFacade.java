package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class ScrollPaneFacade extends ActorFacade<ScrollPane, ScrollPane.ScrollPaneStyle> {
    private ManagedAsset<Texture> mBackground, mHorizontalTrack,
        mHorizontalThumb, mVerticalTrack, mVerticalThumb;

    public ScrollPaneFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
    }

    @ExposeToLua
    public void setWidget(ActorFacade<? extends Actor, ?> wrapper) {
        getActor().setWidget(wrapper.getActor());
    }

    @ExposeToLua
    public void layout() {
        getActor().layout();
    }

    @Override
    protected ScrollPane createActor(ScrollPane.ScrollPaneStyle style) {
        return new ScrollPane(null, style);
    }

    @Override
    protected ScrollPane.ScrollPaneStyle createStyle() {
        return new ScrollPane.ScrollPaneStyle();
    }

    @Override
    protected void populateStyle(ScrollPane.ScrollPaneStyle style, LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, BACKGROUND, mBackground);
        mHorizontalTrack = swapStyleImage(styleTable, HORIZONTAL_TRACK, mHorizontalTrack);
        mHorizontalThumb = swapStyleImage(styleTable, HORIZONTAL_THUMB, mHorizontalThumb);
        mVerticalTrack = swapStyleImage(styleTable, VERTICAL_TRACK, mVerticalTrack);
        mVerticalThumb = swapStyleImage(styleTable, VERTICAL_THUMB, mVerticalThumb);

        style.background = toDrawable(mBackground);
        style.hScroll = toDrawable(mHorizontalTrack);
        style.hScrollKnob = toDrawable(mHorizontalThumb);
        style.vScroll = toDrawable(mVerticalTrack);
        style.vScrollKnob = toDrawable(mVerticalThumb);
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        mHorizontalTrack = freeAsset(mHorizontalTrack);
        mHorizontalThumb = freeAsset(mHorizontalThumb);
        mVerticalTrack = freeAsset(mVerticalTrack);
        mVerticalThumb = freeAsset(mVerticalThumb);
        super.dispose();
    }
}
