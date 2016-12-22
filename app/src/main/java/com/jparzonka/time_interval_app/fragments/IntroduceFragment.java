package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-19.
 */

public class IntroduceFragment extends Fragment {

    protected View view;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.introduce_layout, container, false);
        Toast.makeText(view.getContext(), "Let me introduce someone", Toast.LENGTH_SHORT).show();
        return view;
    }
}
