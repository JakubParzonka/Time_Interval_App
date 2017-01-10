package com.jparzonka.time_interval_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.time_interval_app.data.BitSetHandler;
import com.jparzonka.time_interval_app.data.DTO;
import com.jparzonka.time_interval_app.fragments.FrequencyModeFragment;
import com.jparzonka.time_interval_app.fragments.TimeIntervalModeFragment;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

/**
 * Created by Jakub on 2016-12-26.
 */
public class SendDataActivity extends AppCompatActivity {
    private static boolean isExternalClockSelected = false;
    private CheckBox externalClockCheckbox;
    // TODO wartość selectedMode'a zrobić jako ENUM!!
    private String selectedMode = "";
    private UsbInterface intf;
    private static TimeIntervalModeFragment timeIntervalModeFragment;
    private static FrequencyModeFragment frequencyModeFragment;
    private static CheckBox checkBoxA, checkBoxB, checkBoxCW;
    private DTO dto;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static D2xxManager d2xxManager;
    private static FT_Device ftDevice;
    private int openIndex = 0;
    int currentIndex = -1;
    private static final String SERIAL_NUMBER = "FTS9MKOJ";
    private static UsbDevice usbDeviceT5300;
    private static FT_Device ftDev;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            d2xxManager = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException ex) {
            ex.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_data_layout);
        CrashManager.register(this, BuildConfig.HOCKEYAPP_APP_ID, new CrashManagerListener() {
            @Override
            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);
        this.registerReceiver(mUsbReceiver, filter);

        if (d2xxManager != null)
            Toast.makeText(this, "MA: D2xxManager not null", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "MA: D2xxManager is null", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initConnection();
        }
        externalClockCheckbox = (CheckBox) findViewById(R.id.external_clock_checkbox);
        externalClockCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalClockCheckbox.isChecked()) {
                    setExternalClockSelected(true);
                    Log.i("SendDataActivity", "setExternalClockSelected is true");
                } else if (!externalClockCheckbox.isChecked()) {
                    setExternalClockSelected(false);
                }
            }
        });
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mode_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                // find which radio button is selected
                if (checkedId == R.id.ti_radio_button) {
                    setSelectedMode("TI");
                    timeIntervalModeFragment = new TimeIntervalModeFragment();
                    fragmentTransaction.replace(R.id.operation_mode_fragment, timeIntervalModeFragment).commit();
                    Log.i("SendDataActivity", "fragment replaced on TimeIntervalModeFragment");
                } else if (checkedId == R.id.frequency_radio_button) {
                    setSelectedMode("F");
                    frequencyModeFragment = new FrequencyModeFragment();
                    fragmentTransaction.replace(R.id.operation_mode_fragment, frequencyModeFragment).commit();
                    Log.i("SendDataActivity", "fragment replaced on FrequencyModeFragment");
                }
            }
        });
        checkBoxA = (CheckBox) findViewById(R.id.signal_A_polarization);
        checkBoxB = (CheckBox) findViewById(R.id.signal_B_polarization);
        checkBoxCW = (CheckBox) findViewById(R.id.signal_CW_polarization);
        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                dto = new DTO();
                //    if(dto.)
                handleSending(dto);
            }
        });

        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                BitSet reset = BitSetHandler.getRESET_Address();
//                byte[] outData = reset.toByteArray();
//                new MenuActivity().sendDataToGenerator(outData);
            }
        });

        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            String s = device.getSerialNumber();
            if (Objects.equals(s, SERIAL_NUMBER)) {
                //Toast.makeText(this, "Ciastko", Toast.LENGTH_SHORT).show();
                setUsbDeviceT5300(device);
            }
//            Toast.makeText(this, device.getSerialNumber(), Toast.LENGTH_SHORT).show();
        }
        try {
            //   Toast.makeText(this, "T5300U: " + getUsbDeviceT5300().getSerialNumber(), Toast.LENGTH_SHORT).show();
            intf = usbDeviceT5300.getInterface(0);
            //   Toast.makeText(this, "Interface name: " + intf.getName(), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(this, "SDA: There is no FTDI device attached!", Toast.LENGTH_SHORT).show();
        }
        int i = d2xxManager.createDeviceInfoList(this);
        Toast.makeText(this, "SDA: device info list " + String.valueOf(i), Toast.LENGTH_SHORT);

        int devCount = deviceList.size();
        if (devCount > 0) {
            Toast.makeText(this, "SDA: devCount > 0", Toast.LENGTH_SHORT).show();
            ftDev = new FT_Device(this, mUsbManager, usbDeviceT5300, intf);
            //ftDev = d2xxManager.openByUsbDevice(this, usbDeviceT5300);
            //TODO tryOpen rzuca false!!
            try {
                Toast.makeText(this, "SDA/tryOpen: " + d2xxManager.tryOpen(this, ftDev, null), Toast.LENGTH_SHORT).show();
            } catch (NullPointerException npe) {
                Toast.makeText(this, "SDA: Try open failed", Toast.LENGTH_SHORT).show();
            }
            //  ftDev = d2xxManager.openByIndex(this, currect_index);
            if (ftDev == null) {
                Toast.makeText(this, "SDA: ftDev == null", Toast.LENGTH_SHORT).show();
            } else {
                //ftDev = d2xxManager.openByUsbDevice(this, usbDeviceT5300);
                Toast.makeText(this, "SDA: ftDev != null", Toast.LENGTH_SHORT).show();
            }
            try {
                if (ftDev.isOpen()) {
                    Toast.makeText(this, "SDA: ftDev is open", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "SDA: tDev is not open", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                Toast.makeText(this, "SDA: isOpen() throws null :/", Toast.LENGTH_SHORT).show();
            }
        } else
            Toast.makeText(this, "SDA: devCount =< 0", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initConnection() {

    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ShowToast")
    private void handleSending(DTO dto) {

        BitSetHandler s = new BitSetHandler(dto);

        sendDataToGenerator(s.getSET_S_Address());
        sendDataToGenerator(s.getSET_S_DataArray());
        sendDataToGenerator(s.getSET_TRIG_Address());
        sendDataToGenerator(s.getSET_TRIG_DataArray());
        sendDataToGenerator(s.getTRIG_DIV_Address());
        sendDataToGenerator(s.getTRIG_DIV_DataArray());
        sendDataToGenerator(s.getSYNTH_N_Address());
        sendDataToGenerator(s.getSYNTH_N_DataArray());
        sendDataToGenerator(s.getADF4360_LOAD_Address());
        sendDataToGenerator(s.getADF4360_LOAD_R_COUNTER_LATCH_DataArray());
        sendDataToGenerator(s.getADF4360_LOAD_Address());
        sendDataToGenerator(s.getADF4360_LOAD_CONTROL_LATCH_DataArray());
        sendDataToGenerator(s.getADF4360_LOAD_Address());
        sendDataToGenerator(s.getADF4360_LOAD_N_COUNTER_LATCH_DataArray());
        sendDataToGenerator(s.getRESET_Address());
        sendDataToGenerator(s.getRESET_POINTLESS_DataArray());
        sendDataToGenerator(s.getCFR1_Address());
        sendDataToGenerator(s.getCFR1_DataArray());
        sendDataToGenerator(s.getFTW0_Address());
        sendDataToGenerator(s.getFTW0_DataArray());

    }

    public void sendDataToGenerator(byte[] outputData) {
        try {
            int result = ftDev.write(outputData, outputData.length, true);
            Toast.makeText(this, "MA/SDTG:Data bytes: " + result, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(this, "MA/SDTG: ftDev == null", Toast.LENGTH_SHORT).show();
        }
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

    public static void setUsbDeviceT5300(UsbDevice usbDeviceT5300) {
        SendDataActivity.usbDeviceT5300 = usbDeviceT5300;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String TAG = "FragL";
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.i(TAG, "DETACHED...");
            }
        }
    };

}
