package com.jparzonka.time_interval_app.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.TabActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.time_interval_app.R;

public class SendDataFragment extends Fragment {
    private View view;
    private static Context deviceContext;
    private D2xxManager ftdiD2xxManager;
    private FT_Device ftDevice = null;
    private RadioGroup operationModeRadioGroup;
    private RadioButton tiRadioButton, frequencyRadioButton;
    private NumberPicker secondsPicker, miliSecondsPicker, microSecondsPicker, nanoSecondsPicker, pikoSecondsPicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_data, container, false);
        Toast.makeText(view.getContext(), "Let's send some data!", Toast.LENGTH_SHORT).show();

        operationModeRadioGroup = (RadioGroup) view.findViewById(R.id.operation_mode_radio_group);
        operationModeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ti_mode_radio_button) {
                    Toast.makeText(view.getContext(), "TI", Toast.LENGTH_SHORT).show();
                } else if (checkedId == R.id.frequency_mode_radio_button) {
                    Toast.makeText(view.getContext(), "FREQUENCY", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // TODO => ustalić min i max wartość dla każdego z nich
        secondsPicker = (NumberPicker) view.findViewById(R.id.s_number_picker);
        miliSecondsPicker = (NumberPicker) view.findViewById(R.id.m_s_number_picker);
        microSecondsPicker = (NumberPicker) view.findViewById(R.id.micro_s_number_picker);
        nanoSecondsPicker = (NumberPicker) view.findViewById(R.id.n_s_number_picker);
        pikoSecondsPicker = (NumberPicker) view.findViewById(R.id.p_s_number_picker);

        secondsPicker.setWrapSelectorWheel(true);
        miliSecondsPicker.setWrapSelectorWheel(false);
        microSecondsPicker.setWrapSelectorWheel(false);
        nanoSecondsPicker.setWrapSelectorWheel(false);
        pikoSecondsPicker.setWrapSelectorWheel(false);

        return view;
    }

    public SendDataFragment() {
    }

    @SuppressLint("ValidFragment")
    public SendDataFragment(D2xxManager ftdiD2xxManager, FT_Device ftDevice) {
        this.ftdiD2xxManager = ftdiD2xxManager;
        this.ftDevice = ftDevice;
    }



        private class TabClass extends TabActivity{

    }
}
