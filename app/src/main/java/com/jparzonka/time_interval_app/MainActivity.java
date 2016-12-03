package com.jparzonka.time_interval_app;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jparzonka.mylibrary.j2xx.D2xxManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView textView, textView2,textView3,textView4,textView5;

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.device_name);
        textView4 = (TextView) findViewById(R.id.vendor_id);
        textView5 = (TextView) findViewById(R.id.product_itd);
        System.out.println(Boolean.valueOf(String.valueOf(0)));
        try {
            D2xxManager manager = D2xxManager.getInstance(getApplicationContext());
            textView.setText(String.valueOf(D2xxManager.getLibraryVersion()));
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }


        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();

        Set<String> devicesNames = deviceList.keySet();
        for (String s : devicesNames) {
            System.out.println("Name: " + s);
        }

        Collection<UsbDevice> collection = deviceList.values();

        Iterator<UsbDevice> iterator = collection.iterator();
        while (iterator.hasNext()) {
            UsbDevice d = iterator.next();
            textView3.setText("Device name: " + d.getDeviceName());
            textView4.setText("Vendor id: " + d.getVendorId());
            textView5.setText("Product id: " + d.getProductId());

        }

        textView2.setText(String.valueOf(deviceList.size()));

//        UsbDevice usbDevice = deviceList.get("deviceName");
//        UsbInterface usbInterface;
//
//        //ZAINICJOWAĆ TEN OBIEKT!
//        FT_Device device = new FT_Device(getApplicationContext(), usbManager, );

    }
}
