package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;

import java.util.zip.ZipFile;

/**
 * Maps file names to {@link FileHandle} objects within zip files.
 *
 * TODO: Handle multi-part zip files and translation packs
 */
public class ZipFileHandleResolver implements FileHandleResolver {
    private ZipFile zipFile;

    public ZipFileHandleResolver(ZipFile zipFile) {
        this.zipFile = zipFile;
    }

    @Override
    public FileHandle resolve(String fileName) {
        return new ZipFileHandle(zipFile, fileName);
    }
}
