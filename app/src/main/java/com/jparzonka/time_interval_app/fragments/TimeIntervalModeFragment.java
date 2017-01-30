package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.jparzonka.time_interval_app.R;
import com.jparzonka.time_interval_app.data.ByteArrayForSYNTH_N;

/**
 * Created by Jakub on 2016-12-26.
 */

public class TimeIntervalModeFragment extends Fragment {

    private View view;
    private static double outputWidth = 0;
    private static FrequencyTriggerSectionFragment frequencyTriggerSectionFragment;
    private static PeriodTriggerSectionFragment periodTriggerSectionFragment;
    private static CheckBox checkBoxA, checkBoxB, checkBoxCW;
    private byte[] timeInterval;
    private static boolean isPeriodTriggerSectionSelected;
    public static boolean isTriggerSelected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ti_mode_layout, container, false);

//        sTIEditText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                sTIEditText.setText("");
//                return false;
//            }
//        });
        Spinner tiSpinner = (Spinner) view.findViewById(R.id.time_interval_mode_spinner);
        ArrayAdapter<CharSequence> tiAdapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.ti_mode_array, android.R.layout.simple_spinner_item);
        tiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tiSpinner.setAdapter(tiAdapter);
        tiSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TIMF/TI/onItemSelected", String.valueOf(parent.getItemAtPosition(position)));
                setTimeInterval(getTIValueFromSpinner(parent.getItemAtPosition(position), position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Spinner outputWidthSpinner = (Spinner) view.findViewById(R.id.output_width_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.ow_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        outputWidthSpinner.setAdapter(adapter);
        outputWidthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("TIMF/OW/onItemSelected", String.valueOf(parent.getItemAtPosition(position)));
                setOutputWidth(getOWValueFromSpinner(parent.getItemAtPosition(position), position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        RadioGroup triggerRadioGroup = (RadioGroup) view.findViewById(R.id.trigger_radio_group);
        triggerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.period_trigger_section_radio_button) {
                    isTriggerSelected = true;
                    setPeriodTriggerSectionSelected(true);
                    periodTriggerSectionFragment = new PeriodTriggerSectionFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.trigger_section_frame_layout, periodTriggerSectionFragment).commit();
                    Log.i("TIMF", "fragment replaced on PeriodTriggerSectionFragment");
                } else if (checkedId == R.id.frequency_trigger_section_radio_button) {
                    setPeriodTriggerSectionSelected(false);
                    isTriggerSelected = true;
                    frequencyTriggerSectionFragment = new FrequencyTriggerSectionFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.trigger_section_frame_layout, frequencyTriggerSectionFragment).commit();
                    Log.i("TIMF", "fragment replaced on FrequencyTriggerSectionFragment");
                }
            }
        });

        checkBoxA = (CheckBox) view.findViewById(R.id.signal_A_polarization);
        checkBoxB = (CheckBox) view.findViewById(R.id.signal_B_polarization);
        checkBoxCW = (CheckBox) view.findViewById(R.id.signal_CW_polarization);
        return view;
    }

    private double getOWValueFromSpinner(Object itemAtPosition, int position) {
        double d;
        switch (position) {
            case 0:
                d = 10E-9;
                break;
            case 1:
                d = 20E-9;
                break;
            case 2:
                d = 50E-9;
                break;
            case 3:
                d = 100E-9;
                break;
            default:
                d = 0;
        }
        Log.i("TIMF/getOWValue", String.valueOf(d));
        return d;
    }

    private byte[] getTIValueFromSpinner(Object itemAtPosition, int position) {
        byte[] array;
        switch (position) {
            case 0:
                array = ByteArrayForSYNTH_N.array10ns;
                break;
            case 1:
                array = ByteArrayForSYNTH_N.array1micros;
                break;
            case 2:
                array = ByteArrayForSYNTH_N.array10micros;
                break;
            case 3:
                array = ByteArrayForSYNTH_N.array100micros;
                break;
            case 4:
                array = ByteArrayForSYNTH_N.array1ms;
                break;
            case 5:
                array = ByteArrayForSYNTH_N.array10ms;
                break;
            case 6:
                array = ByteArrayForSYNTH_N.array100ms;
                break;
            case 7:
                array = ByteArrayForSYNTH_N.array1s;
                break;
            default:
                array = ByteArrayForSYNTH_N.array0;
        }
        Log.i("TIMF/getTIValue", "size of array = " + String.valueOf(array.length));
        return array;
    }

    public static double getOutputWidth() {
        return outputWidth;
    }

    private void setOutputWidth(double outputWidth) {
        TimeIntervalModeFragment.outputWidth = outputWidth;
    }

    public void setPeriodTriggerSectionSelected(boolean periodTriggerSectionSelected) {
        isPeriodTriggerSectionSelected = periodTriggerSectionSelected;
    }

    public static boolean getPeriodTriggerSectionSelected() {
        return isPeriodTriggerSectionSelected;
    }

    public static FrequencyTriggerSectionFragment getFrequencyTriggerSectionFragment() {
        return frequencyTriggerSectionFragment;
    }

    public static PeriodTriggerSectionFragment getPeriodTriggerSectionFragment() {
        return periodTriggerSectionFragment;
    }

    public boolean hasSignal_A_InvertedPolarization() {
        return checkBoxA.isChecked();
    }

    public boolean hasSignal_B_InvertedPolarization() {
        return checkBoxB.isChecked();
    }

    public boolean hasSignal_CW_InvertedPolarization() {
        return checkBoxCW.isChecked();
    }


    public byte[] getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(byte[] timeInterval) {
        this.timeInterval = timeInterval;
    }
}
