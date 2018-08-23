package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

import android.util.Log;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Author;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Error;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.TokenID;
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
 * Created by rogerh on 7/21/2018.
 */

public class RequestAuthorLogin extends AppNetworkRequest {

    public static final String TAG = RequestRegisterAuthor.class.getSimpleName();

    private static final String url = ToDoRestAPIs.getBaseUrl() + ToDoRestAPIs.login;

    public RequestAuthorLogin(APICallbackListener apiCallbackListener, Object jsonRequestBody){
        super(apiCallbackListener);
        RequestBody requestBody = RequestBody.create(MediaType.parse(CONTENT_TYPE), jsonRequestBody.toString());
        request = new Request.Builder()
                .url(url)
                .addHeader(CONTENT_TYPE, JSON_CONTENT_TYPE)
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
            public void onFailure(Call call, final IOException e) {

                Log.e(TAG, e.getMessage());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallbackListener.onFailureCallback(e.getMessage());
                    }
                });
            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {

                try {
                    if(response.code() == 201){
                        responseObject = new GsonBuilder().create().fromJson(response.body().string(), TokenID.class);
                    }
                    else {
                        responseObject = new Error(response.code(), response.message());
                    }
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    responseObject = new Error(102, e.getMessage());
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        apiCallbackListener.onSuccessCallback(REQUEST_TYPE.REQUEST_LOGIN_AUTHOR, responseObject);
                    }
                });


            }
        });
    }
}
