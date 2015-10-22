package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.files.FileHandleStream;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * A file handle to an entry within a zip file.
 */
public class ZipFileHandle extends FileHandleStream {
    private ZipFile mZipFile;
    private ZipEntry mZipEntry;

    /**
     * Creates a new zip entry handle.
     * @param zipFile The zip file which contains the entry.
     * @param entryPath The path to the entry within the zip file.
     */
    public ZipFileHandle(ZipFile zipFile, String entryPath) {
        super(entryPath);
        mZipFile = zipFile;
        mZipEntry = zipFile.getEntry(entryPath);
    }

    @Override
    public boolean exists() {
        return mZipEntry != null;
    }

    @Override
    public boolean isDirectory() {
        return exists() && mZipEntry.isDirectory();
    }

    @Override
    public long length() {
        if (exists()) {
            return mZipEntry.getSize();
        } else {
            return 0;
        }
    }

    @Override
    public long lastModified() {
        if (exists()) {
            return mZipEntry.getTime();
        } else {
            return 0;
        }
    }

    @Override
    public InputStream read() {
        try {
            return mZipFile.getInputStream(mZipEntry);
        } catch (Exception ex) {
            throw new GdxRuntimeException("Error reading zip file entry: " + mZipEntry.getName(), ex);
        }
    }
}
