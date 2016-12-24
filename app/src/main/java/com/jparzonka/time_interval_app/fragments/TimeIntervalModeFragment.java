package com.jparzonka.time_interval_app.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;

import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-24.
 */

public class TimeIntervalModeFragment extends Fragment {
    private View view;
    private NumberPicker secondsPicker, miliSecondsPicker, microSecondsPicker, nanoSecondsPicker, pikoSecondsPicker;

    public TimeIntervalModeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send_data, container, false);

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
}
