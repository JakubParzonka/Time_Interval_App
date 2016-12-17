package com.jparzonka.time_interval_app;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

public class InfoPanel extends AppCompatActivity {

    private TextView textView1, textView2, textView3, textView4, textView5, deviceNameTextView, vendroIdTextView, productIdTextView, libraryVersiontextView, isOpentextView;
    UsbDevice device = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_panel);

        textView1 = (TextView) findViewById(R.id.textView1);
        deviceNameTextView = (TextView) findViewById(R.id.textView21);

        textView2 = (TextView) findViewById(R.id.textView2);
        vendroIdTextView = (TextView) findViewById(R.id.textView22);

        textView3 = (TextView) findViewById(R.id.textView3);
        productIdTextView = (TextView) findViewById(R.id.textView23);

        textView4 = (TextView) findViewById(R.id.textView4);
        libraryVersiontextView = (TextView) findViewById(R.id.textView24);

        textView5 = (TextView) findViewById(R.id.textView5);
        isOpentextView = (TextView) findViewById(R.id.textView25);

        UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbFactory factory = new UsbFactory(usbManager);

        textView5.setText("Size: " + UsbFactory.getDeviceListSize());

        try {
             device = factory.getUsbDevice();
        } catch (NullPointerException npe) {
            Log.e("ERROR", "Device is not connected!", npe);
        }

        try {
            String deviceName = device.getDeviceName();
            deviceNameTextView.setText(deviceName);
        } catch (NullPointerException npe) {
            deviceNameTextView.setText("DN got crashed");
            Log.e("ERROR", "Device name is NULL", npe);
        }

        try {
            int deviceVendorId = device.getVendorId();
            vendroIdTextView.setText(deviceVendorId);
        } catch (NullPointerException npe) {
            vendroIdTextView.setText("VId got crashed");
            Log.e("ERROR", "Vendor ID is NULL", npe);
        }

        try {
            int deviceProductId = device.getProductId();
            productIdTextView.setText(deviceProductId);
        } catch (NullPointerException npe) {
            productIdTextView.setText("PId got crashed");
            Log.e("ERROR", "Product ID ID is NULL", npe);
        }


    }


}
//            deviceNameTextView.setText(factory.getUsbDevice().getDeviceName());
//            vendroIdTextView.setText(factory.getUsbDevice().getVendorId());
//            productIdTextView.setText(factory.getUsbDevice().getProductId());
//            libraryVersiontextView.setText("Library version");
//            isOpentextView.setText("Is device open?");