package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jparzonka.time_interval_app.R;

public class SendDataFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_data, container, false);
        Toast.makeText(view.getContext(), "Let's send some data!", Toast.LENGTH_SHORT).show();
        return view;
    }
}
