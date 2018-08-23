package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Author;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rogerh on 7/15/2018.
 */

public class ToDoJSONParser {
    public static Author getAuthor(JSONObject jsonObject) {
        Author author = new Author();
        try {
            author.setAuthorId(jsonObject.getLong("authorId"));
            author.setAuthorName(jsonObject.getString("authorName"));
            author.setAuthorEmailId(jsonObject.getString("authorEmailId"));
            author.setAuthorPassword(jsonObject.getString("authorPassword"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return author;
    }
}
