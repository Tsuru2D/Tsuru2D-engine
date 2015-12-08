package com.tsuru2d.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.PlatformApi;
import com.tsuru2d.engine.loader.RawAssetLoader;
import com.tsuru2d.engine.loader.zip.ZipRawAssetLoader;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

public class FileModel implements Serializable {
    private String mFileFolder;
    private Hashtable<String, GameInfo> mMetaData;
    public static FileModel mInstance;

    /////////test////////
    private ArrayList<GameInfo> test = new ArrayList<>();
    private int mInt = 0;
    ////////*test////////

    private FileModel() {
        mFileFolder = System.getProperty("user.home") + "\\Tsuru-Game\\";
        File fileFolder = new File(mFileFolder);
        if (!fileFolder.exists()) {
            fileFolder.mkdir();
            new File(mFileFolder + "/game/").mkdir();
            mMetaData = new Hashtable<>();
            saveMetadata();
        } else {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(mFileFolder + "config.conf"));
                mMetaData = (Hashtable<String, GameInfo>)objectInputStream.readObject();
                System.out.println("MetaData successfully loaded");
                objectInputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static FileModel getInstance() {
        if (mInstance == null)
            mInstance = new FileModel();
        return mInstance;
    }

    public ArrayList<GameInfo> downloadedList() {
        Enumeration keys = mMetaData.keys();
        ArrayList<GameInfo> list = new ArrayList<GameInfo>();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            list.add(mMetaData.get(key));
        }
        return list;
    }

    public ArrayList<GameInfo> cloudList() {
        ArrayList<GameInfo> cloudList = new ArrayList<>();
        String url = "http://awsassets.panda.org/img/original/google__small.png";
        try{

            ArrayList<Map<String,String>> rawList = Distribution.generateList();
            for(Map<String, String> map : rawList){
                cloudList.add(new GameInfo(map.get("gamename"), map.get("author"), "com.tusuru." + map.get("gamename"), map.get("key"), new ImageIcon(new URL(url))));
            }
//            String url = "http://awsassets.panda.org/img/original/google__small.png";
//            test.add(new GameInfo("hello" + mInt++, "me", "com.oxycode.myvisualnovel.vngame.zip", "x", new ImageIcon(new URL(url))));
//            return test;
        }catch (Exception e) {
            e.printStackTrace();
        }

        return cloudList;
    }

    public boolean downloadFile(GameInfo gameInfo) {
        String filePath = mFileFolder + "game\\" + gameInfo.mFileName;
        if(Distribution.download(filePath, gameInfo.mKey)) {
            mMetaData.put(gameInfo.mName, gameInfo);
            saveMetadata();
            return true;
        }
        return false;
    }

    public String openFile(GameInfo gameInfo) throws Exception {
        saveMetadata();
      if (!mMetaData.containsKey("name"))
          throw new Exception("No Game File named: " + "name");
        String filePath = mFileFolder + "game/" + gameInfo.mFileName;
        System.out.println(filePath);
        File gameZipFile = new File(filePath);
        RawAssetLoader rawAssetLoader = new ZipRawAssetLoader(gameZipFile, null);
        PlatformApi api = new PlatformApi(null, null, rawAssetLoader);
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        new LwjglApplication(new EngineMain(api), config);
        return null;
    }

    public void uploadFile(File file, String url, String author, String name) {
        Distribution.upload(file, url, author, name);
    }

    private void saveMetadata() {
        try{
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(mFileFolder + "config.conf"));
            objectOutputStream.writeObject(mMetaData);
        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class GameInfo implements Serializable {
        public String mName;
        public String mAuthor;
        public String mFileName;
        public String mKey;
        public ImageIcon mImageIcon;

        public GameInfo(String name, String author, String fileName, String key, ImageIcon imageIcon) {
            mName = name;
            mAuthor = author;
            mFileName = fileName;
            mKey = key;
            mImageIcon = imageIcon;
        }

        public String toString() {
            return mName + " " + mAuthor + " " + mFileName;
        }
    }
}

