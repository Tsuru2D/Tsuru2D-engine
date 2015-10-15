package com.tsuru2d.engine.util;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileHandle extends FileHandle {
    private ZipEntry mZipEntry;
    private final ZipFile mZipFile;

    public ZipFileHandle(ZipFile zipFile, String innerFilePath) {
        super(innerFilePath.replace('\\', '/'), Files.FileType.Classpath);
        mZipFile = zipFile;
        mZipEntry = mZipFile.getEntry(innerFilePath.replace('\\', '/'));
    }

    @Override
    public ZipFileHandle child (String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) return new ZipFileHandle(mZipFile, name);
        return new ZipFileHandle(mZipFile, new File(file, name).toString());
    }

    @Override
    public ZipFileHandle sibling (String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) throw new GdxRuntimeException("Cannot get the sibling of the root.");
        return new ZipFileHandle(mZipFile, new File(file.getParent(), name).toString());
    }

    @Override
    public ZipFileHandle parent () {
        File parent = file.getParentFile();
        if (parent == null) {
            if (type == Files.FileType.Absolute)
                parent = new File("/");
            else
                parent = new File("");
        }
        return new ZipFileHandle(mZipFile, parent.toString());
    }

    @Override
    public InputStream read () {
        try {
            return mZipFile.getInputStream(mZipEntry);
        } catch (IOException e) {
            throw new GdxRuntimeException("File not found: " + file + " (Archive)");
        }
    }

    @Override
    public boolean exists() {
        return mZipEntry != null;
    }

    @Override
    public long length () {
        return mZipEntry.getSize();
    }
}