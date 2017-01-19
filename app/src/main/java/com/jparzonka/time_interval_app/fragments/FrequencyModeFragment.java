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

public class FrequencyModeFragment extends Fragment {
    private View view;
    private double frequencyInMHz;
    private int period;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frequency_mode_layout,
                container, false);
        Spinner frequencySpinner = (Spinner) view.findViewById(R.id.frequency_mode_spinner);
        ArrayAdapter<CharSequence> fAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.frequency_mode_array, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(fAdapter);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FMF/F/onItemSelected", String.valueOf(parent.getItemAtPosition(position)));
                setFrequencyInMHz(getFrequencyValueFromSpinner(parent.getItemAtPosition(position), position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner periodSpinner = (Spinner) view.findViewById(R.id.frequency_period_mode_spinner);
        ArrayAdapter<CharSequence> tiAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.freqency_period_array, android.R.layout.simple_spinner_item);
        tiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodSpinner.setAdapter(tiAdapter);
        periodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("FMF/FP/onItemSelected", String.valueOf(parent.getItemAtPosition(position)));
                setFrequencyInMHz(getFrequencyPeriodCountFromSpinner(parent.getItemAtPosition(position), position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return view;
    }

    private double getFrequencyValueFromSpinner(Object itemAtPosition, int position) {
        double d;
        switch (position) {
            case 0:
                d = 10E+9;
                break;
            case 1:
                d = 25E+9;
                break;
            case 2:
                d = 50E+9;
                break;
            case 3:
                d = 75E+9;
                break;
            default:
                d = 0;
        }
        Log.i("FMF/getFMValue", String.valueOf(d));
        return d;
    }

    private double getFrequencyPeriodCountFromSpinner(Object itemAtPosition, int position) {
        double d;
        switch (position) {
            case 0:
                d = 10;
                break;
            case 1:
                d = 100;
                break;
            case 2:
                d = 1000;
                break;
            case 3:
                d = 10000;
                break;
            default:
                d = 0;
        }
        Log.i("FMF/getFMPValue", String.valueOf(d));
        return d;
    }

    public double getFrequencyInMHz() {
        return frequencyInMHz;
    }

    private void setFrequencyInMHz(double frequencyInMHz) {
        Log.i("FMF/setFrequencyInMHz", String.valueOf(frequencyInMHz));
        this.frequencyInMHz = frequencyInMHz;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        Log.i("FMF/setPeriod", String.valueOf(period));
        this.period = period;
    }
}
