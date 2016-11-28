package com.jparzonka.mylibrary.j2xx;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;
import java.nio.ByteBuffer;
import java.util.concurrent.Semaphore;

/* compiled from: FT_Device */
class BulkInWorker implements Runnable {
    UsbEndpoint mBulkInEndpoint;
    UsbDeviceConnection mConnection;
    FT_Device mDevice;
    int mNrBuf;
    Semaphore mPauseLock;
    boolean mPaused;
    ProcessInCtrl mProInCtrl;
    int mReadTimeout;
    int mTransSize;

    BulkInWorker(FT_Device dev, ProcessInCtrl inCtrl, UsbDeviceConnection connection, UsbEndpoint endpoint) {
        this.mDevice = dev;
        this.mBulkInEndpoint = endpoint;
        this.mConnection = connection;
        this.mProInCtrl = inCtrl;
        this.mNrBuf = this.mProInCtrl.getParams().getBufferNumber();
        this.mTransSize = this.mProInCtrl.getParams().getMaxTransferSize();
        this.mReadTimeout = this.mDevice.getDriverParameters().getReadTimeout();
        this.mPauseLock = new Semaphore(1);
        this.mPaused = false;
    }

    void pause() throws InterruptedException {
        this.mPauseLock.acquire();
        this.mPaused = true;
    }

    void restart() {
        this.mPaused = false;
        this.mPauseLock.release();
    }

    boolean paused() {
        return this.mPaused;
    }

    public void run() {
        int bufferIndex = 0;
        do {
            try {
                if (this.mPaused) {
                    this.mPauseLock.acquire();
                    this.mPauseLock.release();
                }
                InBuffer inBuf = this.mProInCtrl.acquireWritableBuffer(bufferIndex);
                if (inBuf.getLength() == 0) {
                    ByteBuffer buffer = inBuf.getInputBuffer();
                    buffer.clear();
                    inBuf.setBufferId(bufferIndex);
                    int totalBytesRead = this.mConnection.bulkTransfer(this.mBulkInEndpoint, buffer.array(), this.mTransSize, this.mReadTimeout);
                    if (totalBytesRead > 0) {
                        buffer.position(totalBytesRead);
                        buffer.flip();
                        inBuf.setLength(totalBytesRead);
                        this.mProInCtrl.releaseReadableBuffer(bufferIndex);
                    }
                }
                bufferIndex = (bufferIndex + 1) % this.mNrBuf;
            } catch (InterruptedException e) {
                try {
                    this.mProInCtrl.releaseWritableBuffers();
                    this.mProInCtrl.purgeINData();
                    return;
                } catch (Exception e2) {
                    Log.d("BulkIn::", "Stop BulkIn thread");
                    e2.printStackTrace();
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                Log.e("BulkIn::", "Fatal error in BulkIn thread");
                return;
            }
        } while (!Thread.interrupted());
        try {
            throw new InterruptedException();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
