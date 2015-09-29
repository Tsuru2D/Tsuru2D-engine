package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.utils.Array;
import com.tsuru2d.engine.loader.FileFinder;

import java.io.File;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipFileFinder implements FileFinder {
    private static class ZipEntryNode {
        private final String mName;
        private final Array<ZipEntryNode> mChildren;

        public ZipEntryNode(String name, boolean isFile) {
            mName = name;
            if (isFile) {
                mChildren = null;
            } else {
                mChildren = new Array<ZipEntryNode>();
            }
        }

        public ZipEntryNode get(String name) {
            for (ZipEntryNode node : mChildren) {
                if (node.mName.equalsIgnoreCase(name)) {
                    return node;
                }
            }
            return null;
        }

        public String findByFileName(String nameWithoutExtension) {
            for (int i = 0; i < mChildren.size; ++i) {
                String sourceName = mChildren.get(i).mName;
                String name = sourceName;
                int extensionIndex = name.lastIndexOf('.');
                if (extensionIndex >= 0) {
                    name = name.substring(0, extensionIndex);
                }
                if (name.equalsIgnoreCase(nameWithoutExtension)) {
                    return sourceName;
                }
            }
            return null;
        }

        public ZipEntryNode put(String name, boolean isFile) {
            ZipEntryNode node = get(name);
            if (node == null) {
                node = new ZipEntryNode(name, isFile);
                mChildren.add(node);
            }
            return node;
        }
    }

    private final ZipEntryNode mRootNode;

    public ZipFileFinder(ZipFile zipFile) {
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
        mRootNode = rootNode;
    }

    private ZipEntryNode getParentDirNode(File parentDir) {
        File parentParent = parentDir.getParentFile();
        if (parentParent == null) {
            return mRootNode;
        }
        ZipEntryNode node = getParentDirNode(parentParent);
        node = node.get(parentDir.getName());
        return node;
    }

    public String findFileName(File parentDir, String name) {
        ZipEntryNode node = getParentDirNode(parentDir);
        return node.findByFileName(name);
    }
}
