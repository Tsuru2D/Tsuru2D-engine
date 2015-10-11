package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.tsuru2d.engine.loader.AssetID;
import com.tsuru2d.engine.loader.AssetType;
import com.tsuru2d.engine.loader.LuaFileLoader;
import com.tsuru2d.engine.loader.MetadataLoader;
import com.tsuru2d.engine.model.MetadataInfo;
import com.tsuru2d.engine.loader.RawAssetLoader;
import com.tsuru2d.engine.util.Xlog;
import org.luaj.vm2.LuaTable;

import java.io.File;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipRawAssetLoader implements RawAssetLoader, FileHandleResolver {
    private final ZipFile mMainZipFile;
    private final Map<String, ZipFile> mLanguagePackFiles;
    private final AssetManager mAssetManager;
    private final Map<AssetID, String> mResolvePathCache;
    private String mLanguageCode;
    private MetadataInfo mMetadataInfo;
    private ZipEntryNode mZipRootNode;

    public ZipRawAssetLoader(ZipFile mainFile, ZipFile... languagePacks) {
        mMainZipFile = mainFile;
        mLanguagePackFiles = null; // TODO: create map of language -> zipfile
        mAssetManager = new AssetManager(this);
        mAssetManager.setLoader(LuaTable.class, new LuaFileLoader(this));
        mZipRootNode = buildNodeTree(mainFile);
        mResolvePathCache = new HashMap<AssetID, String>();
        mAssetManager.load("metadata.lua", LuaTable.class);
    }

    @Override
    public <T> void startLoadingRaw(AssetID rawAssetID, Class<T> type, AssetLoaderParameters<T> params) {
        String path = findPath(rawAssetID);
        mResolvePathCache.put(rawAssetID, path);
        mAssetManager.load(path, type, params);
        Xlog.d("Started loading asset: %s", rawAssetID);
    }

    @Override
    public void finishLoadingRaw(AssetID rawAssetID) {
        mAssetManager.finishLoadingAsset(mResolvePathCache.get(rawAssetID));
    }

    @Override
    public void unloadRaw(AssetID rawAssetID) {
        String path = mResolvePathCache.remove(rawAssetID);
        mAssetManager.unload(path);
        Xlog.d("Unloading asset: %s", rawAssetID);
    }

    @Override
    public void update() {
        mAssetManager.update();
    }

    @Override
    public MetadataInfo getMetadata() {
        if (mMetadataInfo != null) {
            return mMetadataInfo;
        }
        mAssetManager.finishLoadingAsset("metadata.lua");
        LuaTable metadataTable = mAssetManager.get("metadata.lua");
        mMetadataInfo = MetadataLoader.getMetadata(metadataTable);
        return mMetadataInfo;
    }

    @Override
    public List<AssetID> setLanguage(String languageCode) {
        mLanguageCode = languageCode;
        return new ArrayList<AssetID>() {{ // TODO
            add(AssetID.TEXT);
        }};
    }

    @Override
    public String getLanguage() {
        return mLanguageCode;
    }

    @Override
    public FileHandle resolve(String fileName) {
        // TODO: handle multiple zips
        return new ZipFileHandle(mMainZipFile, fileName);
    }

    private ZipEntryNode getParentDirNode(File parentDir) {
        File parentParent = parentDir.getParentFile();
        if (parentParent == null) {
            return mZipRootNode.get(parentDir.getName());
        }
        ZipEntryNode node = getParentDirNode(parentParent);
        node = node.get(parentDir.getName());
        return node;
    }

    private String findFileName(File parentDir, String name) {
        ZipEntryNode node = getParentDirNode(parentDir);
        return node.findByFileName(name);
    }

    private String findPath(AssetID rawAssetID) {
        File path = getLocalizedRootPath(rawAssetID.getType());
        String[] subPaths = rawAssetID.getPath();
        for (int i = 0; i < subPaths.length - 1; ++i) {
            path = new File(path, subPaths[i]);
        }
        path = new File(path, findFileName(path, subPaths[subPaths.length - 1]));
        return path.getPath();
    }

    private File getLocalizedRootPath(AssetType type) {
        return new File(getMetadata().mAssetDirs.get(type));
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
