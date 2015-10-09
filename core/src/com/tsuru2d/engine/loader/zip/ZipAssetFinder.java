package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.files.FileHandle;
import com.tsuru2d.engine.loader.AssetFinder;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetType;

import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * TODO: Handle multi-part zip files and translation packs
 */
public class ZipAssetFinder extends AssetFinder {
    private final ZipFile mZipFile;
    private final ZipEntryNode mRootNode;
    private String mLanguageCode;

    public ZipAssetFinder(ZipFile zipFile) {
        mZipFile = zipFile;
        mRootNode = buildNodeTree(zipFile);
    }

    public void setLanguage(String languageCode) {
        if (languageCode.equals(mLanguageCode)) {
            return;
        }

        mLanguageCode = languageCode;
        mAssetLoader.invalidatePath(AssetID.TEXT);
        // TODO: Invalidate observers
    }

    public void setTypePathMap(Map<AssetType, String> typePathMap) {
        mTypePathMap = typePathMap;
    }

    private ZipEntryNode getParentDirNode(File parentDir) {
        File parentParent = parentDir.getParentFile();
        if (parentParent == null) {
            return mRootNode.get(parentDir.getName());
        }
        ZipEntryNode node = getParentDirNode(parentParent);
        node = node.get(parentDir.getName());
        return node;
    }

    public String findFileName(File parentDir, String name) {
        ZipEntryNode node = getParentDirNode(parentDir);
        return node.findByFileName(name);
    }

    @Override
    protected void onLanguageChanged() {

    }

    @Override
    public FileHandle resolve(String fileName) {
        return new ZipFileHandle(mZipFile, fileName);
    }

    @Override
    public String findPath(AssetID rawAssetID) {
        File path = getLocalizedRootPath(rawAssetID.getType());
        String[] subPaths = rawAssetID.getPath();

        for (int i = 0; i < subPaths.length - 1; ++i) {
            path = new File(path, subPaths[i]);
        }
        path = new File(path, findFileName(path, subPaths[subPaths.length - 1]));
        return path.getPath();
    }

    private ZipEntryNode getPathForLanguage() {
        return new File(mLanguagesPath, mLanguageCode);
    }

    private ZipEntryNode getLocalizedRootPath(AssetType type) {
        return new File(getPathForLanguage(), mTypePathMap.get(type));
    }

    private static ZipEntryNode buildNodeTree(ZipFile zipFile) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        ZipEntryNode rootNode = new ZipEntryNode(null, false);
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }

            String[] path = entry.getName().split("/");
            ZipEntryNode node = rootNode;
            int i = 0;
            while (i < path.length - 1) {
                node = node.put(path[i++], false);
            }
            node.put(path[i], true);
        }
        return rootNode;
    }
}
