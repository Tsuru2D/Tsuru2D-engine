package com.tsuru2d.engine.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.zip.ZipFile;

public class DrawableLoader {
    private Skin mSkin;
    private static HashMap<String, DrawableLoader> mLoaders;
    private final ZipFile mZipFile;

    private DrawableLoader(String ZipfileLocation) throws Exception{
        mSkin = new Skin();
        try {
            mZipFile = new ZipFile(ZipfileLocation);
        } catch (Exception e) {
            throw new Exception("No zip file");
        }
    }

    public static DrawableLoader getDrawableLoader(String zipFileLocation) throws FileNotFoundException{
        DrawableLoader drawableLoader = mLoaders.get(zipFileLocation);
        if(drawableLoader == null) {
            try {
                drawableLoader = new DrawableLoader(zipFileLocation);
                mLoaders.put(zipFileLocation,drawableLoader);
            } catch (Exception e) {
                throw new FileNotFoundException("No Zip File at: " + zipFileLocation);
            }
        }
        return drawableLoader;
    }

    public Drawable getDrawable(String pathInZip) {
        Drawable drawable = null;
        try {
            drawable = mSkin.getDrawable(pathInZip);
        } catch (Exception e) {
            ZipFileHandle zipFileHandle = new ZipFileHandle(mZipFile, pathInZip);
            mSkin.add(pathInZip, new Texture(zipFileHandle));
            drawable = mSkin.getDrawable(pathInZip);
        }
        return drawable;
    }
}
