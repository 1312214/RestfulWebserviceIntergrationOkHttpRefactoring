package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

import android.util.Log;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Error;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.ToDoList;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rogerh on 7/22/2018.
 */

public class RequestGetToDoList extends AppNetworkRequest {

    public static final String TAG = RequestGetToDoList.class.getSimpleName();


    private static final String url = ToDoRestAPIs.getBaseUrl() + ToDoRestAPIs.getToDoList
            + AppConfig.getRegisteredSuccessfulAuthor().getAuthorEmailId();

    public RequestGetToDoList(APICallbackListener apiCallbackListener){
        super(apiCallbackListener);
        request = new Request.Builder()
                .url(url)
                .addHeader(TOKEN, AppConfig.getSavedSessionTokenID())
                .get()
                .build();
    }

    @Override
    public void makeBackEndRequest() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,final IOException e) {
                Log.e(TAG + "--onFailure: ", e.getMessage());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallbackListener.onFailureCallback(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    if(response.code() == 200){
                        responseObject = new GsonBuilder().create().fromJson(response.body().string(), ToDoList.class);
                    }
                    else {
                        responseObject = new Error(response.code(), response.message());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    responseObject = new Error(101, e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallbackListener.onSuccessCallback(REQUEST_TYPE.REQUEST_GET_TODOS, responseObject);
                    }
                });

            }
        });
    }
}
