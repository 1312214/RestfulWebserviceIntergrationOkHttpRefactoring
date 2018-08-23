package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.ui.activity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.ui.fragment.BusyDialogFragment;

/**
 * Created by rogerh on 7/21/2018.
 */

public class BaseActivity extends AppCompatActivity {

    DialogFragment  dialogFragment;

    protected void showBusyDialog(String message){
        if(dialogFragment != null && dialogFragment.isVisible()){
            dialogFragment.dismiss();
        }

        dialogFragment = new BusyDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("message", message);
        dialogFragment.setArguments(bundle);
        dialogFragment.setCancelable(false);
        dialogFragment.show(getSupportFragmentManager(), "busydialog");
    }

    protected void dismissBusyDialog(){
        if(dialogFragment.isVisible()){
            dialogFragment.dismiss();
        }
    }



    public void toastMessage(String message, int lastedTime){
        Toast.makeText(this, message, lastedTime).show();
    }

}
