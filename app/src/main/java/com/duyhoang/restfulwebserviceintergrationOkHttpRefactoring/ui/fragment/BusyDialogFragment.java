package com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.duyhoang.restfulwebserviceintergrationOkHttpRefactoring.R;

/**
 * Created by rogerh on 7/23/2018.
 */

public class BusyDialogFragment extends android.support.v4.app.DialogFragment {

    View rootView;
    TextView txtMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_dialog_busy, container);
        txtMessage = rootView.findViewById(R.id.text_message);
        txtMessage.setText(getArguments().getString("message", "Loading"));
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return rootView;
    }

    
}
