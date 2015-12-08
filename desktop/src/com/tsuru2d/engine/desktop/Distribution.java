package com.tsuru2d.engine.desktop;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * This sample demonstrates how to make basic requests to Amazon S3 using
 * the AWS SDK for Java.
 * <p>
 * <b>Prerequisites:</b> You must have a valid Amazon Web Services developer
 * account, and be signed up to use Amazon S3. For more information on
 * Amazon S3, see http://aws.amazon.com/s3.
 * <p>
 * <b>Important:</b> Be sure to fill in your AWS access credentials in
 * ~/.aws/credentials (C:\Users\USER_NAME\.aws\credentials for Windows
 * users) before you try to run this sample.
 */
public class Distribution {

    private static String secretKey = "rZ5hGPmZMeeWFwSAoi9p1+ih2FebqUDGA0T/b1+U";
    private static String accessKey = "AKIAJCESRETAJS5KQFNA";
    private static AmazonS3 s3 = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
    private static Region usWest2;
    private static String VNBucket = "vndist";

    static {
        usWest2 = Region.getRegion(Regions.US_WEST_2);
        s3.setRegion(usWest2);
    }

    public static void main(String[] args)  throws IOException  {
//        File file = new File("D:\\CS196Project\\aws-java-sample\\com.oxycode.myvisualnovel.vngame");
//
//        upload(file, "sample", "Tsuru2Dgroup", "sampleGame");
//        download("d:\\a.vngame", "161");
        ArrayList<Map<String, String>> test = generateList();
    }

//    @para File the FileHandle of the game;
//    @para String the intended url of the game;
//    @para String the author of the game;
//    @para String the name of the game

    public static void upload(File VNgame, String imagUrl, String author, String gameName) {

        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
            .withBucketName(VNBucket));
        int size = objectListing.getObjectSummaries().size();


        ObjectMetadata metadata = new ObjectMetadata();
        metadata.addUserMetadata("imagUrl", imagUrl);
        metadata.addUserMetadata("author", author);
        metadata.addUserMetadata("gameName", gameName);
        s3.putObject(new PutObjectRequest(VNBucket, size + "", VNgame).withMetadata(metadata));
    }


//    @para String the path(include the intended file name) that the game would be;
//    @para String key, namely the index of the game in the s3 server;
//    @return boolean if the download success;

    public static boolean download(String path, String key) {
        InputStream inputStream = s3.getObject(VNBucket, key).getObjectContent();

        OutputStream outputStream = null;

        try {
            // read this file into InputStream

            // write the inputStream to a FileOutputStream
            outputStream =
                new FileOutputStream(new File(path));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }

            System.out.println("Done!");

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
            if (outputStream != null) {
                try {
                    // outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }

            }
        }
        return true;
    }


//    @return ArrayList<Map<String, String>> array of maps of 3 String pairs for each game: imagUrl, author, gameName;

    public static ArrayList<Map<String,String>> generateList() {
        ObjectListing objectListing = s3.listObjects(new ListObjectsRequest()
            .withBucketName(VNBucket));
        ArrayList<Map<String,String>> summary = new ArrayList<Map<String,String>>();
        int size = objectListing.getObjectSummaries().size();
        for (int i = 0; i < size; ++i) {
            summary.add(s3.getObject(VNBucket, i + "").getObjectMetadata().getUserMetadata());
        }
        return summary;
    }


}
