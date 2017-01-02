package com.jparzonka.time_interval_app.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.time_interval_app.MenuActivity;
import com.jparzonka.time_interval_app.R;
import com.jparzonka.time_interval_app.data.BitSetHandler;
import com.jparzonka.time_interval_app.data.DTO;

import java.util.BitSet;

/**
 * Created by Jakub on 2016-12-26.
 */
@SuppressLint("ValidFragment")
public class SendDataFragment extends Fragment {

    private View view;
    private static boolean isExternalClockSelected = false;
    private CheckBox externalClockCheckbox;
    // TODO wartość selectedMode'a zrobić jako ENUM!!
    private String selectedMode = "";

    private static TimeIntervalModeFragment timeIntervalModeFragment;
    private static FrequencyModeFragment frequencyModeFragment;
    private static CheckBox checkBoxA, checkBoxB, checkBoxCW;

    private static Context deviceContext;
    private static D2xxManager d2xxManager;
    private static FT_Device ftDevice;
    private int openIndex = 0;
    int currentIndex = -1;
    //    int devCount = -1;

    UsbDevice device = null;

    private static final String SERIAL_NUMBER = "FTS9MKOJ";

    public static void setParameters(Context contextParent, D2xxManager d2xxManager) {
        deviceContext = contextParent;
        SendDataFragment.d2xxManager = d2xxManager;
    }

    @SuppressLint("ValidFragment")
    public SendDataFragment(FT_Device ft_device) {
        ftDevice = ft_device;
    }

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
        checkBoxA = (CheckBox) view.findViewById(R.id.signal_A_polarization);
        checkBoxB = (CheckBox) view.findViewById(R.id.signal_B_polarization);
        checkBoxCW = (CheckBox) view.findViewById(R.id.signal_CW_polarization);
        Button startButton = (Button) view.findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSending();
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void handleSending() {
        DTO dto = new DTO();
        BitSetHandler s = new BitSetHandler(dto);
        // s.verifyOCT_and_DAC();
        BitSet set_s = s.getSET_S_BitSet();
        byte[] outData = set_s.toByteArray();
        Log.i("PTSF/startButton", "outData size -> " + outData.length);
        for (byte c : outData) {
            System.out.format("%d ", c);
        }
//        if (deviceContext.equals(null)/* || d2xxManager.equals(null)*/)
//            throw new NullPointerException("Context in PTSF is null!");
//        else
//            Log.i("PTSF/C", "Context is not null");
        new MenuActivity().sendDataToGenerator(outData);
//            boolean broadcastStatus = sendMessage(outData);
//            if (broadcastStatus) {
//                Log.i("SendDataFragment/B", "sendMessage ->" + broadcastStatus);
//            }
//          ConnectionHandler connectionHandler = new ConnectionHandler();
//          Log.i("PTSF/startButton", "SendStatus: " + connectionHandler.sendMessage(outData));

    }

    public void connectAndSend(byte[] outData) {

//        if (MenuActivity.getUsbDeviceT5300() == null) {
//            Toast.makeText(deviceContext, "open device port(" + tmpProtNumber + ") NG, ftDevice == null", Toast.LENGTH_SHORT).show();
//            return;
//        }

        if (outData != null)
            Toast.makeText(deviceContext, "SDF: outData size = " + outData.length, Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(deviceContext, "SDF: outData is null", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (ftDevice.isOpen()) {
                currentIndex = openIndex;
                Toast.makeText(deviceContext, "SDF: open device port OK", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(deviceContext, "device not open", Toast.LENGTH_SHORT).show();
                Log.e("j2xx", "SendMessage: device not open");
            }
        } catch (NullPointerException e) {
            Toast.makeText(deviceContext, "SDF: isOpen() throws null :/", Toast.LENGTH_SHORT).show();
        }
//        ftDevice.setLatencyTimer((byte) 16);
//        Log.i("CH/sendMessage", "latencyTimer set");
//        try {
        try {
            int result = ftDevice.write(outData);
            Toast.makeText(deviceContext, "SDF: WRITE: " + result, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(deviceContext, "SDF/CAS: ftDev == null", Toast.LENGTH_SHORT).show();
        }
        //       } catch (NullPointerException npe) {
        //        Toast.makeText(deviceContext, npe.getMessage(), Toast.LENGTH_SHORT).show();}
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
