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

public class FrequencyTriggerSectionFragment extends Fragment {
    private View view;

    private byte[] frequencyTrigger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frequency_trigger_section_layout, container, false);

        Spinner frequencySpinner = (Spinner) view.findViewById(R.id.frequency_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.frequency_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        frequencySpinner.setAdapter(adapter);
        frequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TIMF/OW/onItemSelected", String.valueOf(parent.getItemAtPosition(position)));
                setFrequencyTrigger(getFrequencyValueFromSpinner(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private byte[] getFrequencyValueFromSpinner(int position) {
        byte[] d;
        switch (position) {
            case 0:
                d = new byte[]{(byte) 0xBE, 0x69, 0x00, 0x00};
                break;
            case 1:
                d = new byte[]{(byte) 0x02, (byte) 0x90, 0x00, 0x00};
                break;
            case 2:
                d = new byte[]{(byte) 0x02, 0x00, 0x00, 0x00};
                break;
            default:
                d = new byte[]{(byte) 0x00, 0x00, 0x00, 0x00};

        }
        Log.i("FTSF/getFreqValue", String.valueOf(d));
        return d;
    }

    public byte[] getFrequencyTrigger() {
        return frequencyTrigger;
    }

    public void setFrequencyTrigger(byte[] frequencyTrigger) {
        this.frequencyTrigger = frequencyTrigger;
    }


}
