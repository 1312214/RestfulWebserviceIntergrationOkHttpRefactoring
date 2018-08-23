package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.AppConfig;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.R;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.APICallbackListener;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.AppNetworkRequest;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.RequestRegisterAuthor;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.network.Util;
import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.restbean.Author;
import com.google.gson.GsonBuilder;

import java.lang.ref.WeakReference;

import okhttp3.OkHttpClient;

/**
 * Created by rogerh on 7/15/2018.
 */

public class RegisterDialogFrag extends DialogFragment implements View.OnClickListener, APICallbackListener {

    public static final String TAG = RegisterDialogFrag.class.getSimpleName();

    EditText etUsername, etPassword, etMailID;
    Button btnOk, btnCancel;
    ProgressBar progressBar;

    Author author;
    RegisterListener registerListener;
    WeakReference<Context> contextReference;

    AppNetworkRequest appNetworkRequest;
    
    public interface RegisterListener{
        void onRegisterSuccess(Author author);
        void onRegisterFailure(String message);
    }
    
    public void setRegisterListener(final RegisterListener registerListener){
        this.registerListener = registerListener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light_Dialog);
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
//        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle("Register");
        View rootView = inflater.inflate(R.layout.fragment_dialog_register, container);
        initUI(rootView);
        return rootView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        contextReference = new WeakReference<Context>(getActivity());
    }

    private void initUI(View rootView) {
        etUsername = rootView.findViewById(R.id.edit_register_dialog_username);
        etPassword = rootView.findViewById(R.id.edit_register_dialog_password);
        etMailID = rootView.findViewById(R.id.edit_register_mailID);

        btnOk = rootView.findViewById(R.id.button_register_ok);
        btnCancel = rootView.findViewById(R.id.button_register_cancel);
        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        progressBar = rootView.findViewById(R.id.progressbar_wait);

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()){
                case R.id.button_register_ok: signUp();
                    break;
                case R.id.button_register_cancel: dismiss();
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

    }

    private void signUp() throws Exception{
        String authorName = etUsername.getText().toString();
        String authorPw = etPassword.getText().toString();
        String authorMail = etMailID.getText().toString();
        author = new Author(0, authorName, authorMail, authorPw);
        if(Util.isAppOnline(contextReference.get())){
            progressBar.setVisibility(View.VISIBLE);
            appNetworkRequest = AppNetworkRequest.getRequestInstance(AppNetworkRequest.REQUEST_TYPE.REQUEST_REGISTER_AUTHOR,
                    this, new GsonBuilder().create().toJson(author));
            appNetworkRequest.makeBackEndRequest();

        }// end if
    }





    private void toastMessage(final String msg){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(contextReference.get(), msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSuccessCallback(AppNetworkRequest.REQUEST_TYPE request_type, Object obj) {
        switch (request_type){
            case REQUEST_REGISTER_AUTHOR:
                registerListener.onRegisterSuccess((Author)obj);
                toastMessage("Registered successfully");
                dismiss();
                break;
            case REQUEST_ADD_TODOITEM:
                break;
            case REQUEST_DELETE_TODO:
                break;
            case REQUEST_MODIFY_TODO:
                break;
            case REQUEST_LOGOUT_AUTHOR:
                break;
            case REQUEST_LOGIN_AUTHOR:
                break;
            case REQUEST_GET_TODOS:
                break;
        }
    }

    @Override
    public void onFailureCallback(String message) {
        registerListener.onRegisterFailure(message);
    }
}
