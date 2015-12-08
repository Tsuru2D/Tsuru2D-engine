package com.tsuru2d.engine.desktop;

import java.util.ArrayList;

public class FileTest {
    public static void main(String[] args) {
        FileModel fileModel = FileModel.getInstance();
<<<<<<< HEAD
        //fileModel.downloadFile("Hello World", "com.oxycode.myvisualnovel.vngame.zip", "me");
        //fileModel.downloadFile("Hello-World", "com.oxycode.myvisualnovel.vngame.zip", "me");
        //fileModel.openFile("Hello World");
        //fileModel.openFile("Hello-World");
=======
        //fileModel.downloadFile("Hello World", "HelloWorld.ven", "me");
        //fileModel.downloadFile("Hello-World", "com.oxycode.myvisualnovel.vngame.zip", "me");
        //fileModel.openFile("Hello World");
        FileBroswer fileBroswer = new FileBroswer();
        fileModel.openFile("Hello-World");
>>>>>>> d84f572b69e0a96632f7e5abfa61b0ca5445f726
        ArrayList<FileModel.GameInfo> downloaded = fileModel.downloadedList();
        for(FileModel.GameInfo info : downloaded) {
            System.out.println(info);
        }
    }
}
