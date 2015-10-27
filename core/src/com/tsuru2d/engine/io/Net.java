package com.tsuru2d.engine.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.net.HttpStatus;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonWriter;
import com.tsuru2d.engine.lua.ExposedJavaClass;
import org.luaj.vm2.LuaTable;

import java.io.IOException;
import java.io.StringWriter;

public class Net extends ExposedJavaClass implements NetManager {
    private String mAuthToken;
    private String mGamePackage;

    public Net(String gamePackage){mGamePackage=gamePackage;}

    @Override
    public boolean isLoggedIn() {
        if (mAuthToken==null)
            return false;
        return true;
    }
    @Override
    public void login(String username, String password, final Callback callback) {
        System.out.println(mAuthToken);
        StringWriter stringWriter=new StringWriter();
        JsonWriter jsonWriter=new JsonWriter(stringWriter);
        try{
            jsonWriter.object();
            jsonWriter.name("email").value(username);
            jsonWriter.name("password").value(password);
            jsonWriter.pop();
        }catch (IOException e){
            e.printStackTrace();
        }
        String jsonStr=stringWriter.toString();
        HttpRequest request=new HttpRequest("POST");
        String URL=NetManager.URL.LOGIN.getUrl();
        request.setUrl(URL);
        request.setContent(jsonStr);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode=httpResponse.getStatus().getStatusCode();
                Result result;
                if(statusCode!=HttpStatus.SC_OK){
                    result=Result.ERROR_CONNECTION_FAILED;
                }else {
                    String responseJson = httpResponse.getResultAsString();
                    Json json=new Json(JsonWriter.OutputType.json);
                    json.fromJson(String.class,boolean.class,responseJson);
                    JsonReader jsonReader=new JsonReader();
                    boolean success=jsonReader.parse(responseJson).getBoolean("success");
                    System.out.println(success);
                    if(success) {
                        result = Result.SUCCESS;
                        mAuthToken=jsonReader.parse(responseJson).getString("auth_token");
                        System.out.println(mAuthToken);
                    }else {
                        String error=jsonReader.parse(responseJson).getString("error");
                        if(error.equals(Result.ERROR_INCORRECT_PASSWORD.strValue())){
                            result=Result.ERROR_INCORRECT_PASSWORD;
                        }else{
                            result=Result.ERROR_USER_NOT_FOUND;
                        }
                    }

                }

                callback.onResult(result,null);
            }
            @Override
            public void failed(Throwable t) {     }
            @Override
            public void cancelled() {}
        });
    }




    @Override
    public void logout() {
        mAuthToken=null;
    }

    @Override
    public void enumerateSaveGames(int startIndex, int endIndex, final Callback callback) {
        System.out.println("enum"+mAuthToken);
        StringWriter stringWriter=new StringWriter();
        JsonWriter jsonWriter=new JsonWriter(stringWriter);
        try{
            jsonWriter.object();
            jsonWriter.name("auth_token").value(mAuthToken);
            jsonWriter.name("game_package").value(mGamePackage);
            if(startIndex!=0){jsonWriter.name("from_index").value(startIndex);}
            if(endIndex!=0){jsonWriter.name("to_index").value(endIndex);}
            jsonWriter.pop();
        }catch (IOException e){
            e.printStackTrace();
        }
        String jsonStr=stringWriter.toString();
        HttpRequest request=new HttpRequest("POST");
        String URL=NetManager.URL.ENUMERATESAVE.getUrl();
        request.setUrl(URL);
        request.setContent(jsonStr);
        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept", "application/json");
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                int statusCode=httpResponse.getStatus().getStatusCode();
                Result result;
                GameSaveData[] gameSaveDatas=null;
                if(statusCode!=HttpStatus.SC_OK){
                    result=Result.ERROR_CONNECTION_FAILED;
                }else {
                    String responseJson = httpResponse.getResultAsString();
                    Json json=new Json(JsonWriter.OutputType.json);
                    json.fromJson(String.class,boolean.class,responseJson);
                    JsonReader jsonReader=new JsonReader();
                    boolean success=jsonReader.parse(responseJson).getBoolean("success");
                    System.out.println(success);
                    if(success) {
                        result = Result.SUCCESS;
                        gameSaveDatas=new GameSaveData[jsonReader.parse(responseJson).getChild("saves").size];
                        for (int i = 0; i < gameSaveDatas.length; i++) {
                            gameSaveDatas[i]=new GameSaveData();
                            gameSaveDatas[i].read(new Json(JsonWriter.OutputType.json),
                                jsonReader.parse(responseJson).getChild("saves").get(i));
                        }
                    }else {
                        String error=jsonReader.parse(responseJson).getString("error");
                        if(error.equals(Result.ERROR_INVALID_GAME_PACKAGE.strValue())){
                            result=Result.ERROR_INVALID_GAME_PACKAGE;
                        }else{
                            result=Result.ERROR_NOT_AUTHENTICATED;
                        }
                    }

                }

                callback.onResult(result, gameSaveDatas);
            }
            @Override
            public void failed(Throwable t) {     }
            @Override
            public void cancelled() {}
        });

    }

    @Override
    public void writeSaveGame(GameSaveData data, boolean forceOverwrite, Callback callback) {

    }

    @Override
    public void getConfig(Callback callback) {

    }

    @Override
    public void writeConfig(LuaTable data, Callback callback) {

    }




}
