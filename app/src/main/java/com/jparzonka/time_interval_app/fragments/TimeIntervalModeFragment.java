package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-26.
 */

public class TimeIntervalModeFragment extends Fragment {

    private View view;
    private EditText sTIEditText, msTIEditText, microsTIEditText, nsTIEditText, psTIEditText;
    private int outputWidth = 0;
    private boolean isPeriodTriggerSectionSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ti_mode_layout, container, false);

        sTIEditText = (EditText) view.findViewById(R.id.seconds);
        msTIEditText = (EditText) view.findViewById(R.id.miliseconds);
        microsTIEditText = (EditText) view.findViewById(R.id.microseconds);
        nsTIEditText = (EditText) view.findViewById(R.id.nanoseconds);
        psTIEditText = (EditText) view.findViewById(R.id.picoseconds);


        RadioGroup outputWidthRadioGroup = (RadioGroup) view.findViewById(R.id.output_width_radio_group);
        outputWidthRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ten_radio_button) setOutputWidth(10);
                else if (checkedId == R.id.twenty_radio_button) setOutputWidth(20);
                else if (checkedId == R.id.fifty_radio_button) setOutputWidth(50);
                else if (checkedId == R.id.hundred_radio_button) setOutputWidth(100);
                else
                    Toast.makeText(view.getContext(), "Non of output widths has been selected!", Toast.LENGTH_SHORT).show();

            }
        });

        RadioGroup triggerRadioGroup = (RadioGroup) view.findViewById(R.id.trigger_radio_group);
        triggerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.period_trigger_section_radio_button) {
                    setPeriodTriggerSectionSelected(true);
                    PeriodTriggerSectionFragment periodTriggerSectionFragment = new PeriodTriggerSectionFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.trigger_section_frame_layout, periodTriggerSectionFragment).commit();
                } else if (checkedId == R.id.frequency_trigger_section_radio_button) {
                    setPeriodTriggerSectionSelected(false);
                    FrequencyTriggerSectionFragment frequencyTriggerSectionFragment = new FrequencyTriggerSectionFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.trigger_section_frame_layout, frequencyTriggerSectionFragment).commit();
                }
            }
        });
        return view;
    }


    public int getOutputWidth() {
        return outputWidth;
    }

    private void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public int getsTI() {
        return Integer.parseInt(String.valueOf(sTIEditText.getText()));
    }

    public int getMsTI() {
        return Integer.parseInt(String.valueOf(msTIEditText.getText()));
    }

    public int getMicrosTI() {
        return Integer.parseInt(String.valueOf(microsTIEditText.getText()));
    }

    public int getNsTI() {
        return Integer.parseInt(String.valueOf(nsTIEditText.getText()));
    }

    public int getPsTI() {
        return Integer.parseInt(String.valueOf(psTIEditText.getText()));
    }

    public void setPeriodTriggerSectionSelected(boolean periodTriggerSectionSelected) {
        isPeriodTriggerSectionSelected = periodTriggerSectionSelected;
    }

    public boolean isPeriodTriggerSectionSelected() {
        return isPeriodTriggerSectionSelected;
    }
}
