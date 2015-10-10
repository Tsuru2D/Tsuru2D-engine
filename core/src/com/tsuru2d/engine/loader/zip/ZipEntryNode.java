package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;

/* package */ class ZipEntryNode {
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
        throw new GdxRuntimeException("Cannot find file: " + nameWithoutExtension);
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
