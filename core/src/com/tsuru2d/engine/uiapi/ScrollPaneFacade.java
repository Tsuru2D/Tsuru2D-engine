package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class ScrollPaneFacade extends ActorFacade<ScrollPane> {
    private ManagedAsset<Texture> mBackground, mCorner, mHScroll, mHScrollKnob, mVScroll, mVScrollKnob;

    public ScrollPaneFacade(BaseScreen screen) {
        super(screen);
        ScrollPane scrollPane = createActor();
        setActor(scrollPane);
    }
    @ExposeToLua
    public void setWidget(ActorFacade<? extends Actor> wrapper){
        getActor().setWidget(wrapper.getActor());
    }
    @ExposeToLua
    public void cancel(){
        getActor().cancel();
    }
    @ExposeToLua
    public void setCancelTouchFocus(boolean cancelTouchFocus){
        getActor().setCancelTouchFocus(cancelTouchFocus);
    }
    @ExposeToLua
    public void fling(float flingTime, float velocityX, float velocityY){
        getActor().fling(flingTime, velocityX, velocityY);
    }
    @ExposeToLua
    public float getMaxX(){
        return getActor().getMaxX();
    }
    @ExposeToLua
    public float getMaxY(){
        return getActor().getMaxY();
    }
    @ExposeToLua
    public float getMinHeight(){
        return getActor().getMinHeight();
    }
    @ExposeToLua
    public float getMinWidth(){
        return getActor().getMinWidth();
    }
    @ExposeToLua
    public float getPrefHeight(){
        return getActor().getPrefHeight();
    }
    @ExposeToLua
    public float getPrefWidth (){
        return getActor().getPrefWidth();
    }
    @ExposeToLua
    public float getScrollBarHeight(){
        return getActor().getScrollBarHeight();
    }
    @ExposeToLua
    public float getScrollBarWidth(){
        return getActor().getScrollBarWidth();
    }
    @ExposeToLua
    public float getScrollPercentX(){
        return getActor().getScrollPercentX();
    }
    @ExposeToLua
    public float getScrollPercentY() {
        return getActor().getScrollPercentY();
    }
    @ExposeToLua
    public float getScrollWidth(){
        return getActor().getScrollWidth();
    }
    @ExposeToLua
    public float getScrollHeight(){
        return getActor().getScrollHeight();
    }
    @ExposeToLua
    public float getVelocityX(){
        return getActor().getVelocityX();
    }
    @ExposeToLua
    public float getVelocityY(){
        return getActor().getVelocityY();
    }
    @ExposeToLua
    public float getVisualScrollPercentX(){
        return getActor().getVisualScrollPercentX();
    }
    @ExposeToLua
    public float getVisualScrollPercentY(){
        return getActor().getVisualScrollPercentY();
    }
    @ExposeToLua
    public float getVisualScrollX(){
        return getActor().getVisualScrollX();
    }
    @ExposeToLua
    public float getVisualScrollY(){
        return getActor().getVisualScrollY();
    }
    @ExposeToLua
    public boolean isBottomEdge(){
        return getActor().isBottomEdge();
    }
    @ExposeToLua
    public boolean isDragging(){
        return getActor().isDragging();
    }
    @ExposeToLua
    public boolean isFlinging(){
        return getActor().isFlinging();
    }
    @ExposeToLua
    public boolean isForceScrollX(){
        return getActor().isForceScrollX();
    }
    @ExposeToLua
    public boolean isForceScrollY(){
        return getActor().isForceScrollY();
    }
    @ExposeToLua
    public boolean isLeftEdge(){
        return getActor().isLeftEdge();
    }
    @ExposeToLua
    public boolean isPanning(){
        return getActor().isPanning();
    }
    @ExposeToLua
    public boolean isRightEdge(){
        return getActor().isRightEdge();
    }
    @ExposeToLua
    public boolean isScrollX(){
        return getActor().isScrollX();
    }
    @ExposeToLua
    public boolean isScrollY(){
        return getActor().isScrollY();
    }
    @ExposeToLua
    public boolean isTopEdge(){
        return getActor().isTopEdge();
    }
    @ExposeToLua
    public void layout(){
        getActor().layout();
    }
    @ExposeToLua
    public void scrollTo(float x, float y, float width, float height){
        getActor().scrollTo(x, y, width, height);
    }
    @ExposeToLua
    public void scrollTo(float x, float y, float width, float height, boolean centerHorizontal, boolean centerVertical){
        getActor().scrollTo(x, y, width, height, centerHorizontal, centerVertical);
    }
    @ExposeToLua
    public void setClamp(boolean clamp){
        getActor().setClamp(clamp);
    }
    @ExposeToLua
    public void setFadeScrollBars(boolean fadeScrollBars){
        getActor().setFadeScrollBars(fadeScrollBars);
    }
    @ExposeToLua
    public void setFlickScroll(boolean flickScroll){
        getActor().setFlickScroll(flickScroll);
    }
    @ExposeToLua
    public void setScrollbarsOnTop(boolean scrollBarsOnTop){
        getActor().setScrollbarsOnTop(scrollBarsOnTop);
    }
    @ExposeToLua
    public void setForceScroll(boolean x, boolean y){
        getActor().setForceScroll(x,y);
    }
    @ExposeToLua
    public void setOverscroll(boolean x, boolean y){
        getActor().setOverscroll(x,y);
    }
    @ExposeToLua
    public void setScrollBarPositions(boolean bottom, boolean right){
        getActor().setScrollBarPositions(bottom, right);
    }
    @ExposeToLua
    public void setScrollingDisabled(boolean x, boolean y){
        getActor().setScrollingDisabled(x,y);
    }
    @ExposeToLua
    public void setSmoothScrolling(boolean smoothScrolling){
        getActor().setSmoothScrolling(smoothScrolling);
    }
    @ExposeToLua
    public void setScrollPercentX(float scrollPercentX){
        getActor().setScrollPercentX(scrollPercentX);
    }
    @ExposeToLua
    public void setScrollPercentY(float scrollPercentY){
        getActor().setScrollPercentY(scrollPercentY);
    }
    @ExposeToLua
    public void setVariableSizeKnobs(boolean variableSizeKnobs){
        getActor().setVariableSizeKnobs(variableSizeKnobs);
    }
    @ExposeToLua
    public void updateVisualScroll(){
        getActor().updateVisualScroll();
    }
    @ExposeToLua
    public void setScrollX(float scrollX){
        getActor().setScrollX(scrollX);
    }
    @ExposeToLua
    public void setScrollY(float scrollY){
        getActor().setScrollY(scrollY);
    }
    @ExposeToLua
    public void setVelocityX(float velocityX){
        getActor().setVelocityX(velocityX);
    }
    @ExposeToLua
    public void setVelocityY(float velocityY){
        getActor().setVelocityY(velocityY);
    }


    protected ScrollPane createActor() {
        return new ScrollPane(null,createAndPopulateStyle());
    }

    protected ScrollPane.ScrollPaneStyle createStyle() {
        return new ScrollPane.ScrollPaneStyle();
    }

    protected ScrollPane.ScrollPaneStyle createAndPopulateStyle() {
        ScrollPane.ScrollPaneStyle style = createStyle();
        style.background = getDrawable(mBackground);
        style.corner = getDrawable(mCorner);
        style.hScroll = getDrawable(mHScroll);
        style.hScrollKnob = getDrawable(mHScrollKnob);
        style.vScroll = getDrawable(mVScroll);
        style.vScrollKnob = getDrawable(mVScrollKnob);
        return style;
    }

    @ExposeToLua
    public void setStyle(LuaTable styleTable) {
        dispose();
        mBackground = getAsset(styleTable, "up");
        mCorner = getAsset(styleTable, "down");
        mHScroll = getAsset(styleTable, "hover");
        mHScrollKnob = getAsset(styleTable, "up");
        mVScroll = getAsset(styleTable, "up");
        mVScrollKnob = getAsset(styleTable, "up");
        ScrollPane.ScrollPaneStyle style = createAndPopulateStyle();
        mActor.setStyle(style);
    }


    @Override
    public void dispose() {
        if (mBackground != null) {
            mScreen.getAssetLoader().freeAsset(mBackground);
            mBackground = null;
        }
        if (mCorner != null) {
            mScreen.getAssetLoader().freeAsset(mCorner);
            mCorner = null;
        }
        if (mHScrollKnob != null) {
            mScreen.getAssetLoader().freeAsset(mHScrollKnob);
            mHScrollKnob = null;
        }if (mHScroll != null) {
            mScreen.getAssetLoader().freeAsset(mHScroll);
            mHScroll = null;
        }
        if (mVScroll != null) {
            mScreen.getAssetLoader().freeAsset(mVScroll);
            mVScroll = null;
        }
        if (mVScrollKnob != null) {
            mScreen.getAssetLoader().freeAsset(mVScrollKnob);
            mVScrollKnob = null;
        }
    }

    private ManagedAsset<Texture> getAsset(LuaTable style, String key) {
        if (style.get(key).isnil()) {
            return null;
        } else {
            AssetID assetID = (AssetID)style.get(key).checkuserdata(AssetID.class);
            return mScreen.getAssetLoader().getImage(assetID);
        }
    }

    private TextureRegionDrawable getDrawable(ManagedAsset<Texture> texture) {
        if (texture == null) {
            return null;
        }
        return new TextureRegionDrawable(new TextureRegion(texture.get()));
    }
}
