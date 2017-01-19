package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-26.
 */

public class PeriodTriggerSectionFragment extends Fragment {

    private View view;
    private double periodTrigger;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_period_trigger_section_layout, container, false);

        Spinner periodSpinner = (Spinner) view.findViewById(R.id.period_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.period_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(adapter);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FTSF/OW/onItemSelected", String.valueOf(parent.getItemAtPosition(position)));
                setperiodTrigger(getPeriodValueFromSpinner(parent.getItemAtPosition(position), position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private double getPeriodValueFromSpinner(Object itemAtPosition, int position) {
        double d;
        switch (position) {
            case 0:
                d = 1;
                break;
            case 1:
                d = 10E-3;
                break;
            case 2:
                d = 1E-3;
                break;
            default:
                d = 0;
        }
        Log.i("FTSF/getFreqValue", String.valueOf(d));
        return d;
    }


    public double getperiodTrigger() {
        return periodTrigger;
    }

    public void setperiodTrigger(double periodTrigger) {
        this.periodTrigger = periodTrigger;
    }
}
