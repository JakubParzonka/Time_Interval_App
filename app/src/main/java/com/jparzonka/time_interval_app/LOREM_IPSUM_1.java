package com.jparzonka.time_interval_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Jakub on 2016-12-18.
 */

public class Lorem_Ipsum_1 extends Fragment {

    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lorem_ipsum_1, container, false);

        return view;
    }
}
