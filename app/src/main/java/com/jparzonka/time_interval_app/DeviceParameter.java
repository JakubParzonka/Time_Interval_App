package com.jparzonka.time_interval_app;

/**
 * Created by Jakub on 2016-12-10.
 */

public class DeviceParameter {

    private String deviceName;
    private int vendorID;
    private int productID;
    private int libraryVersion;

    public int getLibraryVersion() {
        return libraryVersion;
    }

    public int getProductID() {
        return productID;
    }

    public int getVendorID() {
        return vendorID;
    }

    public String getDeviceName() {
        return deviceName;
    }

    private void setLibraryVersion(int libraryVersion) {
        this.libraryVersion = libraryVersion;
    }

    private void setProductID(int productID) {
        this.productID = productID;
    }

    private void setVendorID(int vendorID) {
        this.vendorID = vendorID;
    }

    private void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public DeviceParameter() {

    }
}
