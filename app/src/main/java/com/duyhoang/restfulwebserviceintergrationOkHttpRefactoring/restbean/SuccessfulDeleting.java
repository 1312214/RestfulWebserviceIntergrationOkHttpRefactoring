package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean;

/**
 * Created by rogerh on 7/23/2018.
 */

public class SuccessfulDeleting {
    private int code;
    private String message;

    public SuccessfulDeleting(int code, String message){
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
