package com.jparzonka.mylibrary.j2xx;

import android.util.Log;

/* compiled from: FT_Device */
class ProcessRequestWorker implements Runnable {
    int mNrBuf;
    private ProcessInCtrl mProInCtrl;

    ProcessRequestWorker(ProcessInCtrl inCtrl) {
        this.mProInCtrl = inCtrl;
        this.mNrBuf = this.mProInCtrl.getParams().getBufferNumber();
    }

    public void run() {
        int bufferIndex = 0;
        do {
            try {
                InBuffer inBuf = this.mProInCtrl.acquireReadableBuffer(bufferIndex);
                if (inBuf.getLength() > 0) {
                    this.mProInCtrl.processBulkInData(inBuf);
                    inBuf.purge();
                }
                this.mProInCtrl.releaseWritableBuffer(bufferIndex);
                bufferIndex = (bufferIndex + 1) % this.mNrBuf;
            } catch (InterruptedException ex) {
                Log.d("ProcessRequestThread::", "Device has been closed.");
                ex.printStackTrace();
                return;
            } catch (Exception ex2) {
                Log.e("ProcessRequestThread::", "Fatal error!");
                ex2.printStackTrace();
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
