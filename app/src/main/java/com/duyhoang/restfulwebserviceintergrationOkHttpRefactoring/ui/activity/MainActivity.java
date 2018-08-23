package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.R;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.APICallbackListener;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.AppNetworkRequest;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Author;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Error;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.TokenID;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.ui.fragment.RegisterDialogFrag;
import com.google.gson.GsonBuilder;

public class MainActivity extends BaseActivity implements RegisterDialogFrag.RegisterListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, APICallbackListener {



    Button btnLogin;
    EditText etUsername;
    EditText etPassword;
    TextView txtRegister;
    Switch aSwitch;

    AppNetworkRequest appRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        etPassword.setText("an");

        if(AppConfig.getServerEndPointPreference())
            aSwitch.setChecked(true);
        else
            aSwitch.setChecked(false);
        aSwitch.setTextOff("Localhost");
        aSwitch.setTextOn("Remote");

        if(AppConfig.getSavedUsername() != null){
            etUsername.setText(AppConfig.getSavedUsername());
        }
    }

    private void initUI() {
        etUsername = findViewById(R.id.edit_user_name);
        etPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.button_sign_in);
        txtRegister = findViewById(R.id.text_register);
        aSwitch = findViewById(R.id.switch_mode);

        btnLogin.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
        aSwitch.setOnCheckedChangeListener(this);
    }


    @Override
    public void onRegisterSuccess(Author author) {
        AppConfig.saveRegisteredSuccessfulAuthor(new GsonBuilder().create().toJson(author));
        AppConfig.saveUsername(author.getAuthorName());
    }

    @Override
    public void onRegisterFailure(String message) {
        Toast.makeText(this, "Error: - " + message , Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_sign_in: login();
                break;
            case R.id.text_register: showRegistationDialog();
                break;
        }

    }


    private void showRegistationDialog() {
        RegisterDialogFrag registerDialogFrag = new RegisterDialogFrag();
        registerDialogFrag.setRegisterListener(this);
        registerDialogFrag.show(getSupportFragmentManager(), "RegisterDialog");
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if(isChecked){
            AppConfig.setServerEndPointPreference(true);
            AppConfig.seletedEndPoint = AppConfig.API_ENDPOINTS.REMOTE;
        }
        else{
            AppConfig.setServerEndPointPreference(false);
            AppConfig.seletedEndPoint = AppConfig.API_ENDPOINTS.LOCAL;
        }
    }


    @Override
    public void onSuccessCallback(AppNetworkRequest.REQUEST_TYPE request_type, Object obj) {

        try {
            switch (request_type){
                case REQUEST_LOGIN_AUTHOR:
                    AppConfig.saveSessionTokenID(((TokenID)obj).getToken());
                    dismissBusyDialog();
                    goToHomeActivity();
                    break;
                case REQUEST_LOGOUT_AUTHOR:
                    break;
            }
        } catch (ClassCastException e) {
            Error error  = (Error)obj;
            toastMessage(error.getMessage(), Toast.LENGTH_LONG);
        }


    }

    private void goToHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onFailureCallback(String message) {
        toastMessage(message, Toast.LENGTH_SHORT);
    }

    private void login(){
        showBusyDialog("Logging");
        if(isValidAccount()){
            String password = etPassword.getText().toString();
            Author user = AppConfig.getRegisteredSuccessfulAuthor();
            user.setAuthorPassword(password);

            appRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_LOGIN_AUTHOR, this,
                    new GsonBuilder().create().toJson(user));
            appRequest.makeBackEndRequest();
        }
        else {
            toastMessage("Your information account is invalid", Toast.LENGTH_LONG);
        }


    }

    private boolean isValidAccount() {
        String username = etUsername.getText().toString();
        String pw = etPassword.getText().toString();
        if(username == null || username.length() == 0 || pw == null || username.length() == 0)
            return false;
        else
            return true;
    }


}


