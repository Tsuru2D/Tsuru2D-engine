package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.StreamUtils;
import com.tsuru2d.engine.LuaContext;
import com.tsuru2d.engine.loader.*;
import com.tsuru2d.engine.loader.exception.AssetNotFoundException;
import com.tsuru2d.engine.loader.exception.GameLoadException;
import com.tsuru2d.engine.loader.exception.InvalidMetadataException;
import com.tsuru2d.engine.model.GameMetadataInfo;
import com.tsuru2d.engine.model.LangMetadataInfo;
import com.tsuru2d.engine.util.Xlog;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipRawAssetLoader implements RawAssetLoader, FileHandleResolver {
    private final ZipFile mMainZipFile;
    private final Map<String, ZipFile> mLanguagePackFiles;
    private final AssetManager mAssetManager;
    private final Map<AssetType, String> mAssetTypePathMap;
    private final Map<AssetID, String> mResolvePathCache;
    private final GameMetadataInfo mMetadataInfo;
    private String mLanguageCode;
    private ZipEntryNode mZipRootNode;

    public ZipRawAssetLoader(File gameZip, File languagePackDir) {
        try {
            mMainZipFile = new ZipFile(gameZip);
        } catch (IOException e) {
            throw new GameLoadException("Could not load game data", e);
        }
        mLanguagePackFiles = null; // TODO: create map of language -> zipfile
        mZipRootNode = buildNodeTree(mMainZipFile);
        mAssetManager = new AssetManager(this);
        mAssetManager.setLoader(LuaTable.class, new LuaTableLoader(this));
        mAssetManager.setLoader(LuaValue.class, new LuaValueLoader(this));
        mResolvePathCache = new HashMap<AssetID, String>();
        mMetadataInfo = getGameMetadata(mMainZipFile);
        mAssetTypePathMap = mMetadataInfo.mAssetDirs;
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
    public GameMetadataInfo getMetadata() {
        return mMetadataInfo;
    }

    @Override
    public List<String> getAvailableLanguages() {
        return null;
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

    @Override
    public void dispose() {
        if (mAssetManager.getLoadedAssets() > 0) {
            Xlog.e("Undisposed assets: " + mAssetManager.getAssetNames());
        }
        mAssetManager.dispose();
    }

    private void cannotFindAsset(AssetID rawAssetID) {
        throw new AssetNotFoundException("Cannot find asset: " + rawAssetID);
    }

    private String findPath(AssetID rawAssetID) {
        // TODO: handle multiple zips
        String rootPath = mAssetTypePathMap.get(rawAssetID.getType());
        String[] splitRootPath = rootPath.replace('\\', '/').split("/");
        String[] subPath = rawAssetID.getPath();
        StringBuilder sb = new StringBuilder();
        ZipEntryNode curr = mZipRootNode;
        for (String path : splitRootPath) {
            if (!path.isEmpty()) {
                curr = curr.get(path);
                if (curr == null) {
                    cannotFindAsset(rawAssetID);
                }
                sb.append(path);
                sb.append('/');
            }
        }

        for (int i = 0; i < subPath.length - 1; i++) {
            String path = subPath[i];
            curr = curr.get(path);
            if (curr == null) {
                cannotFindAsset(rawAssetID);
            }
            sb.append(path);
            sb.append('/');
        }
        String fileName = curr.findByFileName(subPath[subPath.length - 1]);
        if (fileName == null) {
            cannotFindAsset(rawAssetID);
        }
        sb.append(fileName);
        return sb.toString();
    }

    private static LangMetadataInfo getLangMetadata(ZipFile zipFile) {
        // TODO: fix code duplication
        ZipEntry zipEntry = zipFile.getEntry("metadata.lua");
        if (zipEntry == null) {
            throw new InvalidMetadataException("metadata.lua not found");
        }
        InputStream metadataStream;
        try {
            metadataStream = zipFile.getInputStream(zipEntry);
        } catch (IOException e) {
            throw new InvalidMetadataException("Could not read metadata.lua");
        }
        try {
            LuaTable metadataEnv = new LuaTable();
            LuaContext.load(metadataStream, "metadata.lua", metadataEnv);
            return MetadataLoader.parseLangMetadata(metadataEnv);
        } finally {
            StreamUtils.closeQuietly(metadataStream);
        }
    }

    private static GameMetadataInfo getGameMetadata(ZipFile zipFile) {
        ZipEntry zipEntry = zipFile.getEntry("metadata.lua");
        if (zipEntry == null) {
            throw new InvalidMetadataException("metadata.lua not found");
        }
        InputStream metadataStream;
        try {
            metadataStream = zipFile.getInputStream(zipEntry);
        } catch (IOException e) {
            throw new InvalidMetadataException("Could not read metadata.lua");
        }
        try {
            LuaTable metadataEnv = new LuaTable();
            LuaContext.load(metadataStream, "metadata.lua", metadataEnv);
            return MetadataLoader.parseGameMetadata(metadataEnv);
        } finally {
            StreamUtils.closeQuietly(metadataStream);
        }
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
