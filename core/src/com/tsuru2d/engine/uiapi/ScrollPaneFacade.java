package com.tsuru2d.engine.uiapi;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.tsuru2d.engine.gameapi.BaseScreen;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.ManagedAsset;
import com.tsuru2d.engine.lua.ExposeToLua;
import org.luaj.vm2.LuaTable;

public class ScrollPaneFacade extends ActorFacade<ScrollPane,ScrollPane.ScrollPaneStyle> {
    private ManagedAsset<Texture> mBackground, mCorner, mHorizontalTrack,
                                  mHorizontalThumb, mVerticalTrack, mVerticalThumb;

    public ScrollPaneFacade(BaseScreen screen, AssetID styleID) {
        super(screen, styleID);
    }

    @ExposeToLua
    public void setWidget(ActorFacade<? extends Actor,?> wrapper){
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

    @Override
    protected ScrollPane createActor(ScrollPane.ScrollPaneStyle style) {
        return new ScrollPane(null, style);
    }

    @Override
    protected ScrollPane.ScrollPaneStyle createStyle() {
        return new ScrollPane.ScrollPaneStyle();
    }

    @Override
    protected void populateStyle(ScrollPane.ScrollPaneStyle style,  LuaTable styleTable) {
        mBackground = swapStyleImage(styleTable, "background", mBackground);
        mCorner = swapStyleImage(styleTable, "corner", mCorner);
        mHorizontalTrack = swapStyleImage(styleTable, "horizontalTrack", mHorizontalTrack);
        mHorizontalThumb = swapStyleImage(styleTable, "horizontalThumb", mHorizontalThumb);
        mVerticalTrack = swapStyleImage(styleTable, "verticalTrack", mVerticalTrack);
        mVerticalThumb = swapStyleImage(styleTable, "verticalThumb", mVerticalThumb);

        style.background = toDrawable(mBackground);
        style.corner = toDrawable(mCorner);
        style.hScroll = toDrawable(mHorizontalTrack);
        style.hScrollKnob = toDrawable(mHorizontalThumb);
        style.vScroll = toDrawable(mVerticalTrack);
        style.vScrollKnob = toDrawable(mVerticalThumb);
    }

    @Override
    public void dispose() {
        mBackground = freeAsset(mBackground);
        mCorner = freeAsset(mCorner);
        mHorizontalTrack = freeAsset(mHorizontalTrack);
        mHorizontalThumb = freeAsset(mHorizontalThumb);
        mVerticalTrack = freeAsset(mVerticalTrack);
        mVerticalThumb = freeAsset(mVerticalThumb);
    }
}
