package com.jparzonka.time_interval_app;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jparzonka.mylibrary.j2xx.D2xxManager;

import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by Jakub on 2016-12-18.
 */

public class InfoPanelFragment extends Fragment {
    private TextView deviceNameTextView, productIdTextView, serialNumberTextView, deviceNumberTextView, descriptionNumberTextView, errorTextView;
    private UsbDevice device = null;
    private static D2xxManager d2xxManager;
    private static Context deviceInformationContext;

    public static void setParameters(Context contextParent, D2xxManager d2xxManager) {
        InfoPanelFragment.d2xxManager = d2xxManager;
        deviceInformationContext = contextParent;
    }

    public InfoPanelFragment() {

    }

    public InfoPanelFragment(Context context, D2xxManager d2xxManager) {
        setD2xxManager(d2xxManager);
        setDeviceInformationContext(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_info_panel, container, false);

        setContentView(R.layout.activity_info_panel);
        try {
            deviceNameTextView = (TextView) findViewById(R.id.device_name);
            descriptionNumberTextView = (TextView) findViewById(R.id.description_number);
            productIdTextView = (TextView) findViewById(R.id.product_id);
            serialNumberTextView = (TextView) findViewById(R.id.serial_number);
            deviceNumberTextView = (TextView) findViewById(R.id.device_number);
            errorTextView = (TextView) findViewById(R.id.error_number);

            UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            IntentFilter filter = new IntentFilter();
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            deviceInformationContext.getApplicationContext().registerReceiver(mUsbPlugEvents, filter);
            Log.i("InfoPanelFragment", "in onCreate");
        } catch (Exception e) {
            writeToFile(e.getMessage(), getApplicationContext());
        }


        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart", "");
        try {
            getDeviceInformation();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            writeToFile(e.getMessage(), getApplicationContext());

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "");

        Intent intent = getIntent();
        String action = intent.getAction();

        String hotplug = "android.intent.action.MAIN";
        if (hotplug.equals(action)) {

            try {
                getDeviceInformation();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                String s = e.getMessage();
                if (s != null) {
                    errorTextView.setText(s);
                }
                e.printStackTrace();
            }
        }
    }

    public void getDeviceInformation() throws InterruptedException {
        try {
            int devCount = 0;

            devCount = d2xxManager.createDeviceInfoList(deviceInformationContext);
            Log.i("FtdiModeControl",
                    "Device number = " + Integer.toString(devCount));
            if (devCount > 0) {
                D2xxManager.FtDeviceInfoListNode[] deviceList = new D2xxManager.FtDeviceInfoListNode[devCount];
                d2xxManager.getDeviceInfoList(devCount, deviceList);

                // deviceList[0] = ftdid2xx.getDeviceInfoListDetail(0);

                deviceNumberTextView.setText("Count of devices: " + Integer.toString(devCount));

                if (deviceList[0].serialNumber == null) {
                    serialNumberTextView.setText("Serial number: " + deviceList[0].serialNumber + "(No Serial number)");
                } else {
                    serialNumberTextView.setText("Serial number: " + deviceList[0].serialNumber);
                }

                if (deviceList[0].description == null) {
                    descriptionNumberTextView.setText("Device Description: " + deviceList[0].description + "(No Description)");
                } else {
                    descriptionNumberTextView.setText("Device Description: " + deviceList[0].description);
                }

//
                productIdTextView.setText("Device ID: " + String.format("%08x", deviceList[0].id));
//            int libVersion = D2xxManager.getLibraryVersion();
//            Library.setText("Library Version: " + convertIntToBcdString(libVersion) );

                // display the chip type for the first device
                switch (deviceList[0].type) {
                    case D2xxManager.FT_DEVICE_232B:
                        deviceNameTextView.setText("Device Name : FT232B device");
                        break;

                    case D2xxManager.FT_DEVICE_8U232AM:
                        deviceNameTextView.setText("Device Name : FT8U232AM device");
                        break;

                    case D2xxManager.FT_DEVICE_UNKNOWN:
                        deviceNameTextView.setText("Device Name : Unknown device");
                        break;

                    case D2xxManager.FT_DEVICE_2232:
                        deviceNameTextView.setText("Device Name : FT2232 device");
                        break;

                    case D2xxManager.FT_DEVICE_232R:
                        deviceNameTextView.setText("Device Name : FT232R device");
                        break;

                    case D2xxManager.FT_DEVICE_2232H:
                        deviceNameTextView.setText("Device Name : FT2232H device");
                        break;

                    case D2xxManager.FT_DEVICE_4232H:
                        deviceNameTextView.setText("Device Name : FT4232H device");
                        break;

                    case D2xxManager.FT_DEVICE_232H:
                        deviceNameTextView.setText("Device Name : FT232H device");
                        break;

                    case D2xxManager.FT_DEVICE_X_SERIES:
                        deviceNameTextView.setText("Device Name : FTDI X_SERIES");
                        break;

                    case D2xxManager.FT_DEVICE_4222_0:
                    case D2xxManager.FT_DEVICE_4222_1_2:
                    case D2xxManager.FT_DEVICE_4222_3:
                        deviceNameTextView.setText("Device Name : FT4222 device");
                        break;

                    default:
                        deviceNameTextView.setText("Device Name : FT232B device");
                        break;
                }
            } else {
                deviceNumberTextView.setText("Number of devices: 0");
                deviceNameTextView.setText("Device Name : No device");
                serialNumberTextView.setText("Device Serial Number:");
                descriptionNumberTextView.setText("Device Description:");
//            DeviceLocation.setText("Device Location:");
                productIdTextView.setText("Device ID: ");
//            Library.setText("Library Version: ");

            }
        } catch (Exception e) {
            writeToFile(e.getMessage(), getApplicationContext());
        }

    }

    public void refrestDeviceInformation(View view) {
        try {
            getDeviceInformation();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            String s = e.getMessage();
            if (s != null) {
                errorTextView.setText(s);
            }
            e.printStackTrace();
        }
    }

    private BroadcastReceiver mUsbPlugEvents = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                try {
                    getDeviceInformation();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    writeToFile(e.getMessage(), getApplicationContext());

                    String s = e.getMessage();
                    if (s != null) {
                        errorTextView.setText(s);
                    }
                    e.printStackTrace();
                }
            }
        }
    };

    private void writeToFile(String data, Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("stacktrace.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void onRefreshButtonClick(View view) {
        refrestDeviceInformation(view);
    }


    public static D2xxManager getD2xxManager() {
        return d2xxManager;
    }

    private static void setD2xxManager(D2xxManager d2xxManager) {
        InfoPanelFragment.d2xxManager = d2xxManager;
    }

    public static Context getDeviceInformationContext() {
        return deviceInformationContext;
    }

    private static void setDeviceInformationContext(Context deviceInformationContext) {
        InfoPanelFragment.deviceInformationContext = deviceInformationContext;
    }

}
