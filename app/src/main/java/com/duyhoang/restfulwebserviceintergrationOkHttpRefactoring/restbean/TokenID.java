package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean;

import java.io.Serializable;

/**
 * Created by rogerh on 7/21/2018.
 */

public class TokenID implements Serializable {
    private String token;
    public TokenID(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
