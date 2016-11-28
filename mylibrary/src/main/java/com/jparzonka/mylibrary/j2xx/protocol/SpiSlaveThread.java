package com.jparzonka.mylibrary.j2xx.protocol;

import android.os.Handler;
import android.util.Log;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class SpiSlaveThread extends Thread {
    public static final int THREAD_DESTORYED = 2;
    public static final int THREAD_INIT = 0;
    public static final int THREAD_RUNNING = 1;
    private boolean m_bResponseWaitCheck;
    private boolean m_bSendWaitCheck;
    private int m_iThreadState;
    private Lock m_pMsgLock;
    private Queue<SpiSlaveEvent> m_pMsgQueue;
    private Object m_pResponseWaitCond;
    private Object m_pSendWaitCond;
    private Handler m_pUIHandler;

    protected abstract boolean isTerminateEvent(SpiSlaveEvent spiSlaveEvent);

    protected abstract boolean pollData();

    protected abstract void requestEvent(SpiSlaveEvent spiSlaveEvent);

    public SpiSlaveThread() {
        this.m_pMsgQueue = new LinkedList();
        this.m_pSendWaitCond = new Object();
        this.m_pResponseWaitCond = new Object();
        this.m_pMsgLock = new ReentrantLock();
        this.m_iThreadState = THREAD_INIT;
        setName("SpiSlaveThread");
    }

    public boolean sendMessage(SpiSlaveEvent event) {
        while (this.m_iThreadState != THREAD_RUNNING) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
        this.m_pMsgLock.lock();
        if (this.m_pMsgQueue.size() > 10) {
            this.m_pMsgLock.unlock();
            Log.d("FTDI", "SpiSlaveThread sendMessage Buffer full!!");
            return false;
        }
        this.m_pMsgQueue.add(event);
        if (this.m_pMsgQueue.size() == THREAD_RUNNING) {
            synchronized (this.m_pSendWaitCond) {
                this.m_bSendWaitCheck = true;
                this.m_pSendWaitCond.notify();
            }
        }
        this.m_pMsgLock.unlock();
        if (event.getSync()) {
            synchronized (this.m_pResponseWaitCond) {
                this.m_bResponseWaitCheck = false;
                while (!this.m_bResponseWaitCheck) {
                    try {
                        this.m_pResponseWaitCond.wait();
                    } catch (InterruptedException e2) {
                        this.m_bResponseWaitCheck = true;
                    }
                }
            }
        }
        return true;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        /*
        r8 = this;
        r3 = 1;
        r0 = 0;
        r8.m_iThreadState = r3;
    L_0x0004:
        r3 = java.lang.Thread.interrupted();
        if (r3 != 0) goto L_0x000c;
    L_0x000a:
        if (r0 == 0) goto L_0x0010;
    L_0x000c:
        r3 = 2;
        r8.m_iThreadState = r3;
        return;
    L_0x0010:
        r8.pollData();
        r3 = r8.m_pMsgLock;
        r3.lock();
        r3 = r8.m_pMsgQueue;
        r3 = r3.size();
        if (r3 > 0) goto L_0x0026;
    L_0x0020:
        r3 = r8.m_pMsgLock;
        r3.unlock();
        goto L_0x0004;
    L_0x0026:
        r3 = r8.m_pMsgQueue;
        r2 = r3.peek();
        r2 = (com.ftdi.j2xx.protocol.SpiSlaveEvent) r2;
        r3 = r8.m_pMsgQueue;
        r3.remove();
        r3 = r8.m_pMsgLock;
        r3.unlock();
        r8.requestEvent(r2);
        r3 = r2.getSync();
        if (r3 == 0) goto L_0x0051;
    L_0x0041:
        r4 = r8.m_pResponseWaitCond;
        monitor-enter(r4);
    L_0x0044:
        r3 = r8.m_bResponseWaitCheck;	 Catch:{ all -> 0x005f }
        if (r3 != 0) goto L_0x0056;
    L_0x0048:
        r3 = 1;
        r8.m_bResponseWaitCheck = r3;	 Catch:{ all -> 0x005f }
        r3 = r8.m_pResponseWaitCond;	 Catch:{ all -> 0x005f }
        r3.notify();	 Catch:{ all -> 0x005f }
        monitor-exit(r4);	 Catch:{ all -> 0x005f }
    L_0x0051:
        r0 = r8.isTerminateEvent(r2);
        goto L_0x0004;
    L_0x0056:
        r6 = 100;
        java.lang.Thread.sleep(r6);	 Catch:{ InterruptedException -> 0x005c }
        goto L_0x0044;
    L_0x005c:
        r1 = move-exception;
        r0 = 1;
        goto L_0x0044;
    L_0x005f:
        r3 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x005f }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ftdi.j2xx.protocol.SpiSlaveThread.run():void");
    }
}
