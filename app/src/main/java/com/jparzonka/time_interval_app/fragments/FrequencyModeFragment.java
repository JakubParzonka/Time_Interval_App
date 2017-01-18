package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Toast;

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
        RadioGroup frequencyRadioGroup = (RadioGroup) view.findViewById(R.id.frequency_radio_group);
        frequencyRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_10) setFrequencyInMHz(10);
                else if (checkedId == R.id.radio_button_25) setFrequencyInMHz(25);
                else if (checkedId == R.id.radio_button_50) setFrequencyInMHz(50);
                else if (checkedId == R.id.radio_button_75) setFrequencyInMHz(75);
                else
                    Toast.makeText(view.getContext(), "Non of frequency has been selected!", Toast.LENGTH_SHORT).show();
                Log.i("FMT/checkedId", String.valueOf(checkedId));
            }
        });
        RadioGroup perdiodRadioGroup = (RadioGroup) view.findViewById(R.id.period_radio_group);
        perdiodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_period_10) setPeriod(10);
                else if (checkedId == R.id.radio_button_period_100) setPeriod(100);
                else if (checkedId == R.id.radio_button_period_1000) setPeriod(1000);
                else if (checkedId == R.id.radio_button_period_10000) setPeriod(10000);
                else
                    Toast.makeText(view.getContext(), "Non of frequency has been selected!", Toast.LENGTH_SHORT).show();
                Log.i("FMT/checkedId", String.valueOf(checkedId));
            }
        });


        return view;
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
