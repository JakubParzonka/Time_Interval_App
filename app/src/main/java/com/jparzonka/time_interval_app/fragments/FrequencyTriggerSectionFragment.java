package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-26.
 */

public class FrequencyTriggerSectionFragment extends Fragment {
    private View view;
    private EditText khzFTS, hzFTS, mhzFTS;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frequency_trigger_section_layout, container, false);
        khzFTS = (EditText) view.findViewById(R.id.khz_trigger_section);
        hzFTS = (EditText) view.findViewById(R.id.hz_trigger_section);
        mhzFTS = (EditText) view.findViewById(R.id.mhz_trigger_section);
        return view;
    }

    public int getkhzFTS() {
        return Integer.parseInt(String.valueOf(khzFTS.getText()));
    }

    public int gethzFTS() {
        return Integer.parseInt(String.valueOf(hzFTS.getText()));
    }

    public int getmhzFTS() {
        return Integer.parseInt(String.valueOf(mhzFTS.getText()));
    }
}
