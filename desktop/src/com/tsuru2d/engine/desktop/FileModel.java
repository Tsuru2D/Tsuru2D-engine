package com.tsuru2d.engine.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tsuru2d.engine.EngineMain;
import com.tsuru2d.engine.PlatformApi;
import com.tsuru2d.engine.loader.RawAssetLoader;
import com.tsuru2d.engine.loader.zip.ZipRawAssetLoader;

import java.io.*;
import java.util.*;

public class FileModel implements Serializable {
    private String mFileFolder;
    private Hashtable<String, GameInfo> mMetaData;
    public static FileModel mInstance;

    private FileModel() {
        mFileFolder = System.getProperty("user.home") + "/Tsuru-Game/";
        File fileFolder = new File(mFileFolder);
        if (!fileFolder.exists()) {
            fileFolder.mkdir();
            new File(mFileFolder + "/game/").mkdir();
            mMetaData = new Hashtable<>();
        } else {
            try {
                ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(mFileFolder + "config.conf"));
                mMetaData = (Hashtable<String, GameInfo>)objectInputStream.readObject();
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

    public String[] cloudList() {
        return null;
    }

    public boolean downloadFile(String name, String fileName, String authorName) {
        GameInfo gameInfo = new GameInfo(name, authorName, fileName);
        mMetaData.put(name, gameInfo);
        return true;
    }

    public String openFile(String name) {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(mFileFolder + "config.conf"));
            objectOutputStream.writeObject(mMetaData);
            if (!mMetaData.containsKey(name))
                throw new Exception("No Game File named: " + name);
            String filePath = mFileFolder + "game/" + mMetaData.get(name).mFileName;
            System.out.println(filePath);
            File gameZipFile = new File(filePath);
            RawAssetLoader rawAssetLoader = new ZipRawAssetLoader(gameZipFile, null);
            PlatformApi api = new PlatformApi(null, null, rawAssetLoader);
            LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            new LwjglApplication(new EngineMain(api), config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public class GameInfo implements Serializable {
        public String mName;
        public String mAuthor;
        public String mFileName;

        public GameInfo(String name, String author, String fileName) {
            mName = name;
            mAuthor = author;
            mFileName = fileName;
        }

        public String toString() {
            return mName + " " + mAuthor + " " + mFileName;
        }
    }
}
