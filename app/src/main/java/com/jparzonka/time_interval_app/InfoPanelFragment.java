package com.jparzonka.time_interval_app;

import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class InfoPanelFragment extends Fragment {

    private TextView textView1, textView2, textView3, textView4, textView5, deviceNameTextView, vendroIdTextView, productIdTextView, libraryVersiontextView, isOpentextView;
    private UsbDevice device = null;
    protected View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_info_panel, container, false);
        textView1 = (TextView) view.findViewById(R.id.textView1);
        deviceNameTextView = (TextView) view.findViewById(R.id.textView21);

        textView2 = (TextView) view.findViewById(R.id.textView2);
        vendroIdTextView = (TextView) view.findViewById(R.id.textView22);

        textView3 = (TextView) view.findViewById(R.id.textView3);
        productIdTextView = (TextView) view.findViewById(R.id.textView23);

        textView4 = (TextView) view.findViewById(R.id.textView4);
        libraryVersiontextView = (TextView) view.findViewById(R.id.textView24);

        textView5 = (TextView) view.findViewById(R.id.textView5);
        isOpentextView = (TextView) view.findViewById(R.id.textView25);

        UsbManager usbManager = (UsbManager) view.getContext().getSystemService(Context.USB_SERVICE);
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
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


    }
}



//            deviceNameTextView.setText(factory.getUsbDevice().getDeviceName());
//            vendroIdTextView.setText(factory.getUsbDevice().getVendorId());
//            productIdTextView.setText(factory.getUsbDevice().getProductId());
//            libraryVersiontextView.setText("Library version");
//            isOpentextView.setText("Is device open?");
