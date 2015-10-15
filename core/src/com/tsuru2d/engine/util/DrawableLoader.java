package com.tsuru2d.engine.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;

import java.util.zip.ZipFile;

public class DrawableLoader {
    private Skin mSkin;
    private final ZipFile mZipFile;

    public DrawableLoader(String ZipfileLocation) throws Exception{
        mSkin = new Skin();
        try {
            mZipFile = new ZipFile(ZipfileLocation);
        } catch (Exception e) {
            throw new Exception("No zip file");
        }
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
