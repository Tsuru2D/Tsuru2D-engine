package com.tsuru2d.engine.loader.zip;

import com.badlogic.gdx.utils.Array;

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
        int extensionIndex = nameWithoutExtension.length();
        for (int i = 0; i < mChildren.size; ++i) {
            String name = mChildren.get(i).mName;
            if (name.startsWith(nameWithoutExtension) &&
                (name.length() == extensionIndex || name.charAt(extensionIndex) == '.')) {
                return name;
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
