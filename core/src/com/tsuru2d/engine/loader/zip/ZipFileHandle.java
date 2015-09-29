package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.files.FileHandleStream;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.io.File;
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
        this(zipFile, new File(entryPath));
    }

    /**
     * Creates a new zip entry handle.
     * @param zipFile The zip file which contains the entry.
     * @param entry The path to the entry within the zip file.
     */
    public ZipFileHandle(ZipFile zipFile, File entry) {
        super(entry.getPath());
        mZipFile = zipFile;
        mZipEntry = zipFile.getEntry(entry.getPath());
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

    @Override
    public ZipFileHandle child(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) {
            return new ZipFileHandle(mZipFile, new File(name));
        }
        return new ZipFileHandle(mZipFile, new File(file, name));
    }

    @Override
    public ZipFileHandle sibling(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0) {
            throw new GdxRuntimeException("Cannot get the sibling of the root.");
        }
        return new ZipFileHandle(mZipFile, new File(file.getParent(), name));
    }

    @Override
    public ZipFileHandle parent() {
        File parent = file.getParentFile();
        if (parent == null) {
            if (type == Files.FileType.Absolute) {
                parent = new File("/");
            } else {
                parent = new File("");
            }
        }
        return new ZipFileHandle(mZipFile, parent);
    }
}
