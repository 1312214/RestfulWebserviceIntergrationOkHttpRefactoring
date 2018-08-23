package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

/**
 * Created by rogerh on 7/20/2018.
 */

public interface APICallbackListener {

    void onSuccessCallback(AppNetworkRequest.REQUEST_TYPE request_type, Object obj);
    void onFailureCallback(String message);

}
