package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-26.
 */
public class SendDataFragment extends Fragment {

    private View view;
    private static boolean isExternalClockSelected = false;
    private CheckBox externalClockCheckbox;
    // TODO wartość selectedMode'a zrobić jako ENUM!!
    private String selectedMode = "";

    private static TimeIntervalModeFragment timeIntervalModeFragment;
    private static FrequencyModeFragment frequencyModeFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_data, container, false);
        externalClockCheckbox = (CheckBox) view.findViewById(R.id.external_clock_checkbox);
        externalClockCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalClockCheckbox.isChecked()) {
                    setExternalClockSelected(true);
                    Log.i("SendDataFragment", "setExternalClockSelected is true");
                } else if (!externalClockCheckbox.isChecked()) {
                    setExternalClockSelected(false);
                }
            }
        });


        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.mode_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.ti_radio_button) {
                    setSelectedMode("TI");
                    timeIntervalModeFragment = new TimeIntervalModeFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.operation_mode_fragment, timeIntervalModeFragment).commit();
                    Log.i("SendDataFragment", "fragment replaced on TimeIntervalModeFragment");
                } else if (checkedId == R.id.frequency_radio_button) {
                    setSelectedMode("F");
                    frequencyModeFragment = new FrequencyModeFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.operation_mode_fragment, frequencyModeFragment).commit();
                    Log.i("SendDataFragment", "fragment replaced on FrequencyModeFragment");
                }
            }
        });


        return view;
    }

    public static TimeIntervalModeFragment getTimeIntervalModeFragment() {
        return timeIntervalModeFragment;
    }

    public static FrequencyModeFragment getFrequencyModeFragment() {
        return frequencyModeFragment;
    }

    public static boolean getExternalClockSelected() {
        return isExternalClockSelected;
    }

    public void setExternalClockSelected(boolean externalClockSelected) {
        isExternalClockSelected = externalClockSelected;
    }

    public String getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(String selectedMode) {
        this.selectedMode = selectedMode;
    }
}
