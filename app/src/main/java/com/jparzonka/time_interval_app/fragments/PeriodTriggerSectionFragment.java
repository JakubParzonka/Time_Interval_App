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

public class PeriodTriggerSectionFragment extends Fragment {

    private View view;
    private EditText sPTS, msPTS, microsPTS;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_period_trigger_section_layout, container, false);
        sPTS = (EditText) view.findViewById(R.id.seconds_trigger_section);
        msPTS = (EditText) view.findViewById(R.id.miliseconds_trigger_section);
        microsPTS = (EditText) view.findViewById(R.id.microseconds_trigger_section);
        return view;
    }

    public int getSecondPTS() {
        return Integer.parseInt(String.valueOf(sPTS.getText()));
    }

    public int getMilisecondPTS() {
        return Integer.parseInt(String.valueOf(msPTS.getText()));
    }

    public int getMicrosecondsPTS() {
        return Integer.parseInt(String.valueOf(microsPTS.getText()));
    }
}
