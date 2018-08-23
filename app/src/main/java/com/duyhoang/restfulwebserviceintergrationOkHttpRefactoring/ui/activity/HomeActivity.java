package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.R;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.APICallbackListener;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.AppNetworkRequest;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.Util;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Error;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.ModifiedTodoPayload;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.SuccessfulDeleting;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.ToDoItem;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.ToDoList;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity implements View.OnClickListener, APICallbackListener {

    public static final String TAG = HomeActivity.class.getSimpleName();

    EditText etToDo, etPlace, etToDoID, etModifiedToDoContent;
    Button btnAdd, btnDelete, btnUpdate;
    TextView txtToDoList;

    AppNetworkRequest appRequest;
    List<ToDoItem> todoList;
    ToDoItem ItemToBeDeleted;
    ToDoItem proposedTodo;
    ModifiedTodoPayload payload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if(todoList == null){
            todoList = new ArrayList<ToDoItem>();
        }
        initUI();
        getToDoList();
    }

    private void initUI() {
        etToDo = findViewById(R.id.edit_home_activity_todocontent);
        etPlace = findViewById(R.id.edit_home_activity_place);
        btnAdd = findViewById(R.id.button_home_activity_add);
        txtToDoList = findViewById(R.id.text_home_activity_todolist);
        etToDoID = findViewById(R.id.edit_home_activity_idtodo_modify);
        btnDelete = findViewById(R.id.button_home_activity_delete);
        etModifiedToDoContent = findViewById(R.id.edit_home_activity_modified_todocontent);
        btnUpdate = findViewById(R.id.button_home_activity_update);

        btnAdd.setOnClickListener(this);
        btnUpdate.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_home_activity_add: addNewToDo();
                break;
            case R.id.button_home_activity_update: updateToDo();
                break;
            case R.id.button_home_activity_delete: deleteToDoById();
                break;
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.option_logout: logout();
                return true;
        }
        return false;
    }

    private void logout() {
        startActivity(new Intent(this, MainActivity.class));
        AppConfig.clearSharedPreference();
    }






    private void getToDoList(){
        if(Util.isAppOnline(AppConfig.getContext())){
            showBusyDialog("Getting ToDoList");
            appRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_GET_TODOS, this, null);
            appRequest.makeBackEndRequest();
        }
        else {
            toastMessage("Network issue occured", Toast.LENGTH_SHORT);
        }

    }


    private void addNewToDo() {
        showBusyDialog("Adding a new ToDo");
        String todoContent = etToDo.getText().toString();
        String place = etPlace.getText().toString();
        String authorMailID = AppConfig.getRegisteredSuccessfulAuthor().getAuthorEmailId();
        ToDoItem todo = new ToDoItem(0, todoContent, place, authorMailID);

        if(Util.isAppOnline(AppConfig.getContext())) {
            appRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_ADD_TODOITEM, this,
                    new GsonBuilder().create().toJson(todo));
            appRequest.makeBackEndRequest();
        }



    }


    private void updateToDo() {
        String IdTodoModified = etToDoID.getText().toString();
        String modifiedContent = etModifiedToDoContent.getText().toString();

        ToDoItem currentToDo = getToDoById(Integer.valueOf(IdTodoModified));
        if(currentToDo != null){
            if(Util.isAppOnline(AppConfig.getContext())){
                showBusyDialog("Updating");
                proposedTodo = new ToDoItem(currentToDo.getId(), modifiedContent, currentToDo.getPlace(), currentToDo.getAuthorMail());
                payload = new ModifiedTodoPayload(currentToDo, proposedTodo);

                appRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_MODIFY_TODO, this,
                        new GsonBuilder().create().toJson(payload));
                appRequest.makeBackEndRequest();
            }
            else {
                toastMessage("Network issue", Toast.LENGTH_SHORT);
            }
        }
        else {
            toastMessage("entered ToDo ID is not exist", Toast.LENGTH_SHORT);
        }


    }

    private void deleteToDoById(){
        String todoId = etToDoID.getText().toString();
        ItemToBeDeleted = getToDoById(Integer.valueOf(todoId));
        if(ItemToBeDeleted != null){
            if(Util.isAppOnline(AppConfig.getContext())){
                showBusyDialog("Deleting");
                appRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_DELETE_TODO, this,
                        new GsonBuilder().create().toJson(ItemToBeDeleted));
                appRequest.makeBackEndRequest();
            }
            else{
                toastMessage("Network issue", Toast.LENGTH_SHORT);
            }
        }
        else {
            toastMessage("entered ToDo ID is not exist", Toast.LENGTH_SHORT);
        }
    }


    @Override
    public void onSuccessCallback(AppNetworkRequest.REQUEST_TYPE request_type, Object obj) {
        switch (request_type){
            case REQUEST_ADD_TODOITEM:
                dismissBusyDialog();
                try {
                    addNewToDoItem2ToDoList((ToDoItem)obj);
                } catch (Exception e) {
                    Log.e(TAG + "- onSuccessCallback: ", e.getMessage());
                    toastMessage(((Error)obj).getMessage(), Toast.LENGTH_SHORT);
                }

                break;
            case REQUEST_MODIFY_TODO:
                dismissBusyDialog();
                try {
                    updateModifiedToDoItem(((ToDoItem)obj));
                    updateToDoList();
                    clearAllTextInput();
                } catch (ClassCastException e) {
                    Error error = (Error)obj;
                    toastMessage(error.getMessage(), Toast.LENGTH_SHORT);
                }

                break;
            case REQUEST_GET_TODOS:
                dismissBusyDialog();
                try {
                    setToDoList((ToDoList)obj);
                } catch (Exception e) {
                    Log.e(TAG + "- onSuccessCallback: ", e.getMessage());
                    Error error = (Error)obj;
                    if(error.getCode() == 404){
                        toastMessage("You have nothing to do!", Toast.LENGTH_LONG);
                    }
                    else {
                        toastMessage(error.getMessage(), Toast.LENGTH_SHORT);
                    }
                }
                break;
            case REQUEST_DELETE_TODO:
                dismissBusyDialog();
                if(obj instanceof SuccessfulDeleting){
                    toastMessage("deleted successful", Toast.LENGTH_SHORT);
                    removeToDoItemFromToDoList(ItemToBeDeleted.getId());
                }
                else {
                    Error error = (Error)obj;
                    toastMessage(error.getMessage(), Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    private void updateModifiedToDoItem(ToDoItem todo) {
        for(int i = 0; i < todoList.size(); i++){
            if(todoList.get(i).getId() == todo.getId()){
                todoList.set(i, todo);
                break;
            }
        }
    }

    private void removeToDoItemFromToDoList(int id) {

        for(int i = 0; i < todoList.size(); i++){
            if(todoList.get(i).getId() == id){
                todoList.remove(i);
                break;
            }
        }

        updateToDoList();
        clearAllTextInput();
    }

    private void updateToDoList() {
        txtToDoList.setText("");
        for(ToDoItem item : todoList){
            String line = item.getId() + ", " + item.getTodoString() + ", " + item.getPlace() + "\n";
            txtToDoList.append(line);
        }
    }


    private void setToDoList(ToDoList list) {

            for(ToDoItem item : list){
                todoList.add(item);
                String line = item.getId() + ", " + item.getTodoString() + ", " + item.getPlace() + "\n";
                txtToDoList.append(line);
            }
    }

    private void addNewToDoItem2ToDoList(ToDoItem toDo) {
        todoList.add(toDo);
        String line = toDo.getId() + ", " + toDo.getTodoString() + ", " + toDo.getPlace() + "\n";
        txtToDoList.append(line);
        clearAllTextInput();
    }

    private void clearAllTextInput() {
        etToDo.setText("");
        etPlace.setText("");
        etModifiedToDoContent.setText("");
        etToDoID.setText("");
    }


    @Override
    public void onFailureCallback(String message) {
        toastMessage(message, Toast.LENGTH_SHORT);
    }



    private ToDoItem getToDoById(int id){
        for(ToDoItem item : todoList){
            if(item.getId() == id){
                return item;
            }
        }
        return null;
    }
}
