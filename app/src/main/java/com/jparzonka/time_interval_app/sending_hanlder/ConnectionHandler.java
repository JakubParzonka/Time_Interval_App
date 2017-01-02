package com.jparzonka.time_interval_app.sending_hanlder;

import android.content.Context;
import android.util.Log;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;

/**
 * Created by Jakub on 2016-12-29.
 */

public class ConnectionHandler {

    private static Context deviceContext;
    private static D2xxManager d2xxManager;
    private static FT_Device ftDevice = null;
    private int openIndex = 0;
    private static final String SERIAL_NUMBER = "FTS9MKOJ";

    public static void setParameters(Context contextParent, D2xxManager d2xxManager) {
        deviceContext = contextParent;
        ConnectionHandler.d2xxManager = d2xxManager;
    }

    public ConnectionHandler() {
        if (deviceContext.equals(null)/* || d2xxManager.equals(null)*/)
            throw new NullPointerException("Context in ConnectionHandler is null!");
        else
            Log.i("ConnectionHandler/C", "Context is not null");
        try {
            this.d2xxManager = D2xxManager.getInstance(deviceContext);
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }

        connectWithFTDI();
        Log.i("CH/constructor", "context and d2xxMagager set");
        throw new NullPointerException("Number of devices: " + this.d2xxManager.createDeviceInfoList(deviceContext));
    }

    public void connectWithFTDI() {
        if (ftDevice == null) {
            ftDevice = d2xxManager.openByIndex(deviceContext, openIndex);

            Log.i("CH/connectWithFTDI", "ftDevice toString -> " + ftDevice.toString());
        } else {

            ftDevice = d2xxManager.openByIndex(deviceContext, openIndex);

        }

        if (ftDevice.isOpen()) {
            // read handler
        }
    }


}
