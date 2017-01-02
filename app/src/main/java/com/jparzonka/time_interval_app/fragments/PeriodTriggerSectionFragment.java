package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

        sPTS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                sPTS.setText("");
                return false;
            }
        });
        msPTS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                msPTS.setText("");
                return false;
            }
        });
        microsPTS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                microsPTS.setText("");
                return false;
            }
        });


        return view;
    }

    public double getSecondPTS() {
        double sPT = (Integer.parseInt(sPTS.getText().toString()));
        Log.i("PTSF", "getSecondPTS: " + sPT);
        return sPT;
    }

    public double getMilisecondPTS() {
        double msPT = (Integer.parseInt(msPTS.getText().toString()));
        Log.i("PTSF", "getMilisecondPTS: " + msPT);
        return msPT;
    }

    public double getMicrosecondsPTS() {
        double microsPT = (Integer.parseInt(microsPTS.getText().toString()));
        Log.i("PTSF", "getMicrosecondsPTS: " + microsPT);
        return microsPT;
    }
}
