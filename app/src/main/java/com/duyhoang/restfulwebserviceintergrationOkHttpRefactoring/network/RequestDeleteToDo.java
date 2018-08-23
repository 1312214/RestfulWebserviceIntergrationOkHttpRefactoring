package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

import android.util.Log;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.SuccessfulDeleting;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Error;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by rogerh on 7/23/2018.
 */

public class RequestDeleteToDo extends AppNetworkRequest {

    public static final String TAG = RequestDeleteToDo.class.getSimpleName();

    private static final String url = ToDoRestAPIs.getBaseUrl() + ToDoRestAPIs.deleteToDo;

    public RequestDeleteToDo(APICallbackListener apiCallbackListener, Object jsonRequestBody){
        super(apiCallbackListener);
        RequestBody requestBody = RequestBody.create(MediaType.parse(JSON_CONTENT_TYPE), jsonRequestBody.toString());
        request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, JSON_CONTENT_TYPE)
                .addHeader(TOKEN, AppConfig.getSavedSessionTokenID())
                .delete(requestBody)
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
                Log.e(TAG, e.getMessage());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallbackListener.onFailureCallback(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 204){
                    responseObject = new SuccessfulDeleting(response.code(), response.message());
                }
                else {
                    responseObject = new Error(response.code(), response.message());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallbackListener.onSuccessCallback(REQUEST_TYPE.REQUEST_DELETE_TODO, responseObject);
                    }
                });
            }
        });
    }
}
