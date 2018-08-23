package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;

/**
 * Created by rogerh on 7/16/2018.
 */

public class ToDoRestAPIs {
    public static final String baseLocalHostUrl = "http://10.0.2.2:8080/dotolist/webapi";
    public static final String baseRemoteHostUrl = "http://todolistmobileapp-env.ap-south-1.elasticbeanstalk.com/webapi";

    public static final String registerAuthor="/authors";
    public static final String login = registerAuthor+"/login";
    public static final String addToDo = "/todolists";
    public static final String getToDoList = "/todolists/";
    public static String deleteToDo = "/todolists";

    public static String getBaseUrl(){
        if(AppConfig.seletedEndPoint == AppConfig.API_ENDPOINTS.LOCAL)
            return baseLocalHostUrl;
        else if(AppConfig.seletedEndPoint == AppConfig.API_ENDPOINTS.REMOTE)
            return baseRemoteHostUrl;
        else
            return baseRemoteHostUrl;
    }

}
