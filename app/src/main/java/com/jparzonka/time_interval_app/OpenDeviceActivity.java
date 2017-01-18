package com.jparzonka.time_interval_app;

import android.annotation.TargetApi;
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
import android.widget.TextView;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class OpenDeviceActivity extends AppCompatActivity {

    private D2xxManager d2xxManager;
    private static final String SERIAL_NUMBER = "FTS9MKOJ";
    private static UsbDevice usbDeviceT5300;
    private static FT_Device ftDev;
    private boolean foundDevice;
    private UsbManager usbManager;
    private UsbInterface usbInterface;
    private boolean result;
    private static final String ACTION_USB_PERMISSION =
            "com.android.example.USB_PERMISSION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CrashManager.register(this, BuildConfig.HOCKEYAPP_APP_ID, new CrashManagerListener() {
            @Override
            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });
        try {
            d2xxManager = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException ex) {
            ex.printStackTrace();
        }
        foundDevice = false;
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);
        this.registerReceiver(mUsbReceiver, filter);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_device);
        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);

        Button findButton = (Button) findViewById(R.id.find_device_button);
        findButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                try {
                    setUsbInterface(usbDeviceT5300.getInterface(0));
                } catch (NullPointerException ignored) {
                    ignored.printStackTrace();
                }
                findDevice();
            }
        });

        Button openButton = (Button) findViewById(R.id.open_device_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (foundDevice) {
                    if (openDevice(usbManager, usbInterface, ftDev, d2xxManager.createDeviceInfoList(getApplicationContext()))) {
                        Toast.makeText(v.getContext(), "Device has been opened", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(v.getContext(), SendDataActivity.class);
                        startActivity(intent);
                    } else
                        Toast.makeText(v.getContext(), "Device open problem", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(v.getContext(), "Find device firstly!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void findDevice() {
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        TextView deviceName = (TextView) findViewById(R.id.devices_text_view);
        if (deviceList.size() == 0) {
            deviceName.setText("No device attached");
            setFoundDevice(false);
        }
        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            String s = device.getSerialNumber();
            if (Objects.equals(s, SERIAL_NUMBER)) {
                setUsbDeviceT5300(device);
            }
            deviceName.setText(device.getProductName());
            setFoundDevice(true);
        }


    }

    private boolean openDevice(UsbManager usbManager, UsbInterface usbInterface, FT_Device ftDev, int numberOfDevices) {

        if (numberOfDevices > 0) {
            Toast.makeText(this, "ODA: devCount > 0", Toast.LENGTH_SHORT).show();
//           ftDev = new FT_Device(this, usbManager, usbDeviceT5300, usbInterface);
//          //ftDev = d2xxManager.openByUsbDevice(this, usbDeviceT5300);
            //  ftDev = d2xxManager.openByIndex(this, 0);
            ftDev = d2xxManager.openByUsbDevice(this, usbDeviceT5300);
            Toast.makeText(this, "openDevice/toString: " + d2xxManager.toString(), Toast.LENGTH_LONG).show();
            if (ftDev == null) {
                Toast.makeText(this, "ODA: ftDev == null", Toast.LENGTH_SHORT).show();
                result = false;
            } else {
                setFtDev(ftDev);
//                // Reset FT Device
//                ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
//                // Set Baud Rate
//                ftDev.setBaudRate(115200);
//                // Set Data Bit , Stop Bit , Parity Bit
//                ftDev.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8, D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);
//                // Set Flow Control
//                ftDev.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte) 0x0b, (byte) 0x0d);

                ftDev.setBitMode((byte) 0, D2xxManager.FT_BITMODE_RESET);
                ftDev.setBaudRate(9600);
                ftDev.setDataCharacteristics(D2xxManager.FT_DATA_BITS_8,
                        D2xxManager.FT_STOP_BITS_1, D2xxManager.FT_PARITY_NONE);
                ftDev.setFlowControl(D2xxManager.FT_FLOW_NONE, (byte) 0x00, (byte) 0x00);
                ftDev.setLatencyTimer((byte) 16);
                //ftDev = d2xxManager.openByUsbDevice(this, usbDeviceT5300);
                Toast.makeText(this, "ODA: ftDev != null", Toast.LENGTH_SHORT).show();
                result = true;
            }
        } else {
            Toast.makeText(this, "ODA: devCount =< 0", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);

                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            //call method to set up device communication
                        }
                    } else {
                        Log.d("TAG", "permission denied for device " + device);
                    }
                }
            }
        }


    };

    public void setUsbDeviceT5300(UsbDevice usbDeviceT5300) {
        OpenDeviceActivity.usbDeviceT5300 = usbDeviceT5300;
    }

    public boolean isFoundDevice() {
        return foundDevice;
    }

    public void setFoundDevice(boolean foundDevice) {
        this.foundDevice = foundDevice;
    }

    public static FT_Device getFtDev() {
        return ftDev;
    }

    public static void setFtDev(FT_Device ftDev) {
        OpenDeviceActivity.ftDev = ftDev;
    }

    public void setUsbInterface(UsbInterface usbInterface) {
        this.usbInterface = usbInterface;
    }

}
