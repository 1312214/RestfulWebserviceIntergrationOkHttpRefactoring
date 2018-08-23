package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

import android.content.Context;
import android.os.Handler;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by rogerh on 7/20/2018.
 */

public abstract class AppNetworkRequest implements Runnable {


    public static enum REQUEST_TYPE{
        REQUEST_REGISTER_AUTHOR,
        REQUEST_LOGIN_AUTHOR,
        REQUEST_LOGOUT_AUTHOR,
        REQUEST_ADD_TODOITEM,
        REQUEST_GET_TODOS,
        REQUEST_DELETE_TODO,
        REQUEST_MODIFY_TODO
    }

    public static final int CONNECT_TIME_OUT = 10000;
    public static final int READ_TIME_OUT = 10000;
    public static final String CONTENT_TYPE = "Content-type";
    public static final String JSON_CONTENT_TYPE = "application/json";
    public static final String TOKEN = "token";

    APICallbackListener apiCallbackListener;
    Handler handler;
    Object responseObject;
    Request request;


    public AppNetworkRequest(APICallbackListener apiCallbackListener){
        this.apiCallbackListener = apiCallbackListener;
        handler = new Handler(AppConfig.getContext().getMainLooper());
    }

    public static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MILLISECONDS)
            .readTimeout(READ_TIME_OUT, TimeUnit.MILLISECONDS)
            .build();


    public static AppNetworkRequest getRequestInstance(REQUEST_TYPE request_type,APICallbackListener apiCallbackListener,Object jsonRequestBody){
        AppNetworkRequest appNetworkRequest = null;

        switch (request_type){
            case REQUEST_REGISTER_AUTHOR:
                appNetworkRequest = getRegisterAuthorRequest(apiCallbackListener, jsonRequestBody);
                break;
            case REQUEST_GET_TODOS:
                appNetworkRequest = getGetToDosRequest(apiCallbackListener, jsonRequestBody);
                break;
            case REQUEST_DELETE_TODO:
                appNetworkRequest = getDeleteToDoRequest(apiCallbackListener, jsonRequestBody);
                break;
            case REQUEST_LOGIN_AUTHOR:
                appNetworkRequest = getLoginAuthorRequest(apiCallbackListener, jsonRequestBody);
                break;
            case REQUEST_LOGOUT_AUTHOR:
                appNetworkRequest = getLogoutAuthorRequest(apiCallbackListener, jsonRequestBody);
                break;
            case REQUEST_MODIFY_TODO:
                appNetworkRequest = getModifyTodoRequest(apiCallbackListener, jsonRequestBody);
                break;
            case REQUEST_ADD_TODOITEM:
                appNetworkRequest = getAddToDoItemRequest(apiCallbackListener, jsonRequestBody);
                break;
        }
        return appNetworkRequest;
    }

    private static AppNetworkRequest getAddToDoItemRequest(APICallbackListener apiCallbackListener, Object requestBody) {
        return new RequestAddToDo(apiCallbackListener, requestBody);
    }

    private static AppNetworkRequest getModifyTodoRequest(APICallbackListener apiCallbackListener, Object requestBody) {
        return new RequestModifyToDo(apiCallbackListener, requestBody);
    }

    private static AppNetworkRequest getLogoutAuthorRequest(APICallbackListener apiCallbackListener, Object requestBody) {
        return null;
    }

    private static AppNetworkRequest getLoginAuthorRequest(APICallbackListener apiCallbackListener, Object requestBody) {
        return new RequestAuthorLogin(apiCallbackListener, requestBody);
    }

    private static AppNetworkRequest getDeleteToDoRequest(APICallbackListener apiCallbackListener, Object requestBody) {
        return new RequestDeleteToDo(apiCallbackListener, requestBody);
    }

    private static AppNetworkRequest getGetToDosRequest(APICallbackListener apiCallbackListener, Object requestBody) {
        return new RequestGetToDoList(apiCallbackListener);
    }

    private static AppNetworkRequest getRegisterAuthorRequest(APICallbackListener apiCallbackListener, Object requestBody) {
        return new RequestRegisterAuthor(apiCallbackListener, requestBody);
    }

    public abstract void makeBackEndRequest();


}
