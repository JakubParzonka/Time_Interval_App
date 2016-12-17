package com.jparzonka.time_interval_app;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private TextView textView, textView2, textView3, textView4, textView5;
    private Byte[] bytes;
    private static int TIMEOUT = 0;
    private boolean forceClaim = true;
    private UsbDevice usbDevice;
    private int i = 0;

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

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
        try {
            D2xxManager manager = D2xxManager.getInstance(getApplicationContext());
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }
        textView.setText(String.valueOf(D2xxManager.getLibraryVersion()));


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
            System.out.println("Device name: " + d.getDeviceName());
            System.out.println("Vendor id: " + d.getVendorId());
            System.out.println("Product id: " + d.getProductId());
        }
        textView2.setText(String.valueOf(deviceList.size()));

        //Check a UsbDevice object's attributes, such as product ID, vendor ID, or device class to figure out whether or not you want to communicate with the device.
        usbDevice = deviceList.get("/dev/bus/usb/001/002");

        UsbInterface usbInterface = usbDevice.getInterface(0);
        UsbEndpoint endpoint = usbInterface.getEndpoint(0);
        UsbDeviceConnection connection = usbManager.openDevice(usbDevice);
        connection.claimInterface(usbInterface, forceClaim);

//      ZAINICJOWAĆ TEN OBIEKT!
        FT_Device device = new FT_Device(getApplicationContext(), usbManager, usbDevice, usbInterface);
        D2xxManager.FtDeviceInfoListNode deviceInfo = device.getDeviceInfo();
        System.out.println("FT_DEVICE info: " + deviceInfo.description);
    }

    public void startThread(View view) {
        HandleData hd = new HandleData();
        hd.execute(getUsbDevice());
    }


    private class HandleData extends AsyncTask<UsbDevice, Void, String> {

        protected void onProgressUpdate(Void... values) {
            textView2.setText("Progresik kurde");
        }

        protected String doInBackground(UsbDevice... params) {
            String s = " Hello from AsyncTask";
            System.out.println(s);
            String s1 = params[0].getDeviceName();
            return s + s1;
        }

        protected void onPostExecute(String s) {
            textView.setText(s);
            System.out.println(i);
            i++;
        }
    }
}