package com.jparzonka.time_interval_app;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import java.util.HashMap;

/**
 * Created by Jakub on 2016-12-10.
 */

public class UsbFactory {
    private UsbManager usbManager;
    private UsbDevice usbDevice;
    private UsbInterface usbInterface;
    private UsbEndpoint usbEndpoint;
    private UsbDeviceConnection usbDeviceConnection;
    private HashMap<String, UsbDevice> deviceList;
    private static int deviceListSize;

    public UsbManager getUsbManager() {
        return usbManager;
    }

    public UsbDevice getUsbDevice() {
        return usbDevice;
    }

    public UsbInterface getUsbInterface() {
        return usbInterface;
    }

    public UsbEndpoint getUsbEndpoint() {
        return usbEndpoint;
    }

    public UsbDeviceConnection getUsbDeviceConnection() {
        return usbDeviceConnection;
    }

    private void setUsbManager(UsbManager usbManager) {
        this.usbManager = usbManager;
    }

    private void setUsbDevice(UsbDevice usbDevice) {
        this.usbDevice = usbDevice;
    }

    private void setUsbInterface(UsbInterface usbInterface) throws NullPointerException {
        if (usbInterface.equals(null))
            throw new NullPointerException("Interface in UsbFactory is null");
        else
            this.usbInterface = usbInterface;
    }

    private void setUsbDeviceConnection(UsbDeviceConnection usbDeviceConnection) {
        this.usbDeviceConnection = usbDeviceConnection;
    }

    /**
     * Przed utworzeniem obiektu com.jparzonka.time_interval_app.UsbFactory należy go zainicjować w ten sposób:
     * usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
     * <p>
     * Zakładam, że zostanie już w ten sposób zainicjowany.
     * Należy również podać odpowiednią nazwę urządzenia jako parametr do metody get w setterze UsbDevice
     *
     * @param usbManager
     */
    public UsbFactory(UsbManager usbManager) {
        try {
            setUsbManager(usbManager);
            getDeviceList(usbManager);
            setUsbDevice(getDeviceList(usbManager).get("Time Interval Generator T5200U"));
            setUsbInterface(getUsbDevice().getInterface(0));
            setUsbDeviceConnection(usbManager.openDevice(getUsbDevice()));

        } catch (NullPointerException npe) {
            Log.e("ERROR", "In UsbFactory constructor something is null", npe);
        }
    }


    private HashMap<String, UsbDevice> getDeviceList(UsbManager manager) {
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Log.i("SIZE: ", String.valueOf(deviceList.size()));
        setDeviceListSize(deviceList.size());
        return deviceList;
    }

    public static int getDeviceListSize() {
        return deviceListSize;
    }

    private void setDeviceListSize(int deviceListSize) {
        this.deviceListSize = deviceListSize;
    }
}
