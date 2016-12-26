package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.time_interval_app.DTO.DTO;
import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-26.
 */
public class SendDataFragment extends Fragment {

    private View view;
    private boolean isExternalClockSelected = false;
    private CheckBox externalClockCheckbox;
    // TODO wartość selectedMode'a zrobić jako ENUM!!
    private String selectedMode = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_data, container, false);
        externalClockCheckbox = (CheckBox) view.findViewById(R.id.external_clock_checkbox);
        externalClockCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setExternalClockSelected(true);
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
                    TimeIntervalModeFragment timeIntervalModeFragment = new TimeIntervalModeFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.operation_mode_fragment, timeIntervalModeFragment).commit();
                } else if (checkedId == R.id.frequency_radio_button) {
                    setSelectedMode("F");
                    FrequencyModeFragment frequencyModeFragment = new FrequencyModeFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.operation_mode_fragment, frequencyModeFragment).commit();
                }
            }
        });

        Button startButton = (Button) view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(view.getContext(), "Transmission started", Toast.LENGTH_SHORT).show();
                DTO dto = new DTO();
                System.out.println(dto.toString());
            }
        });

        return view;
    }

    public boolean isExternalClockSelected() {
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
