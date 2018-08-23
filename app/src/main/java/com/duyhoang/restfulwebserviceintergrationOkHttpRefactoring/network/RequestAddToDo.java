package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

import android.os.Looper;
import android.util.Log;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Error;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.ToDoItem;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by rogerh on 7/22/2018.
 */

public class RequestAddToDo extends AppNetworkRequest {

    public static final String TAG = RequestAddToDo.class.getSimpleName();

    private static final String url = ToDoRestAPIs.getBaseUrl() + ToDoRestAPIs.addToDo;

    public RequestAddToDo(APICallbackListener apiCallbackListener, Object jsonRequestBody){
        super(apiCallbackListener);
        RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonRequestBody.toString());
        request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, JSON_CONTENT_TYPE)
                .addHeader(TOKEN, AppConfig.getSavedSessionTokenID())
                .post(requestBody)
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
            public void onResponse(Call call, Response response) {
//                if(Looper.myLooper() == Looper.getMainLooper()) {
//                    Log.e("DUYHOANG", "ON MAIN LOOPER");
//                } else {
//                    Log.e("DUYHOANG", "NOT on Main LOOPER");
//                }

                try {
                    if(response.code() == 200){
                        responseObject = new GsonBuilder().create().fromJson(response.body().string(), ToDoItem.class);
                    }
                    else {
                        responseObject = new Error(response.code(), response.message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    responseObject = new Error(101, e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallbackListener.onSuccessCallback(REQUEST_TYPE.REQUEST_ADD_TODOITEM, responseObject);
                    }
                });
            }
        });
    }
}
