package com.jparzonka.mylibrary.j2xx;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D2xxManager {
    protected static final String ACTION_USB_PERMISSION = "com.jparzonka.mylibrary.j2xx";
    public static final int FTDI_BREAK_OFF = 0;
    public static final int FTDI_BREAK_ON = 16384;
    public static final byte FT_BI = (byte) 16;
    public static final byte FT_BITMODE_ASYNC_BITBANG = (byte) 1;
    public static final byte FT_BITMODE_CBUS_BITBANG = (byte) 32;
    public static final byte FT_BITMODE_FAST_SERIAL = (byte) 16;
    public static final byte FT_BITMODE_MCU_HOST = (byte) 8;
    public static final byte FT_BITMODE_MPSSE = (byte) 2;
    public static final byte FT_BITMODE_RESET = (byte) 0;
    public static final byte FT_BITMODE_SYNC_BITBANG = (byte) 4;
    public static final byte FT_BITMODE_SYNC_FIFO = (byte) 64;
    public static final byte FT_CTS = (byte) 16;
    public static final byte FT_DATA_BITS_7 = (byte) 7;
    public static final byte FT_DATA_BITS_8 = (byte) 8;
    public static final byte FT_DCD = Byte.MIN_VALUE;
    public static final int FT_DEVICE_2232 = 4;
    public static final int FT_DEVICE_2232H = 6;
    public static final int FT_DEVICE_232B = 0;
    public static final int FT_DEVICE_232H = 8;
    public static final int FT_DEVICE_232R = 5;
    public static final int FT_DEVICE_245R = 5;
    public static final int FT_DEVICE_4222_0 = 10;
    public static final int FT_DEVICE_4222_1_2 = 11;
    public static final int FT_DEVICE_4222_3 = 12;
    public static final int FT_DEVICE_4232H = 7;
    public static final int FT_DEVICE_8U232AM = 1;
    public static final int FT_DEVICE_UNKNOWN = 3;
    public static final int FT_DEVICE_X_SERIES = 9;
    public static final byte FT_DSR = (byte) 32;
    public static final byte FT_EVENT_LINE_STATUS = (byte) 4;
    public static final byte FT_EVENT_MODEM_STATUS = (byte) 2;
    public static final byte FT_EVENT_REMOVED = (byte) 8;
    public static final byte FT_EVENT_RXCHAR = (byte) 1;
    public static final byte FT_FE = (byte) 8;
    public static final byte FT_FLAGS_HI_SPEED = (byte) 2;
    public static final byte FT_FLAGS_OPENED = (byte) 1;
    public static final short FT_FLOW_DTR_DSR = (short) 512;
    public static final short FT_FLOW_NONE = (short) 0;
    public static final short FT_FLOW_RTS_CTS = (short) 256;
    public static final short FT_FLOW_XON_XOFF = (short) 1024;
    public static final byte FT_OE = (byte) 2;
    public static final byte FT_PARITY_EVEN = (byte) 2;
    public static final byte FT_PARITY_MARK = (byte) 3;
    public static final byte FT_PARITY_NONE = (byte) 0;
    public static final byte FT_PARITY_ODD = (byte) 1;
    public static final byte FT_PARITY_SPACE = (byte) 4;
    public static final byte FT_PE = (byte) 4;
    public static final byte FT_PURGE_RX = (byte) 1;
    public static final byte FT_PURGE_TX = (byte) 2;
    public static final byte FT_RI = (byte) 64;
    public static final byte FT_STOP_BITS_1 = (byte) 0;
    public static final byte FT_STOP_BITS_2 = (byte) 2;
    private static final String TAG = "D2xx::";
    private static Context mContext;
    private static D2xxManager mInstance;
    private static PendingIntent mPendingIntent;
    private static IntentFilter mPermissionFilter;
    private static List<FtVidPid> mSupportedDevices;
    private static BroadcastReceiver mUsbDevicePermissions;
    private static UsbManager mUsbManager;
    private ArrayList<FT_Device> mFtdiDevices;
    private BroadcastReceiver mUsbPlugEvents;

    /* renamed from: com.jparzonka.mylibrary.j2xx.D2xxManager.1 */
    class C00001 extends BroadcastReceiver {
        C00001() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.hardware.usb.action.USB_DEVICE_DETACHED".equals(action)) {
                UsbDevice dev = (UsbDevice) intent.getParcelableExtra("device");
                FT_Device ftDev = D2xxManager.this.findDevice(dev);
                while (ftDev != null) {
                    ftDev.close();
                    synchronized (D2xxManager.this.mFtdiDevices) {
                        D2xxManager.this.mFtdiDevices.remove(ftDev);
                    }
                    ftDev = D2xxManager.this.findDevice(dev);
                }
            } else if ("android.hardware.usb.action.USB_DEVICE_ATTACHED".equals(action)) {
                D2xxManager.this.addUsbDevice((UsbDevice) intent.getParcelableExtra("device"));
            }
        }
    }

    /* renamed from: com.jparzonka.mylibrary.j2xx.D2xxManager.2 */
    static class C00012 extends BroadcastReceiver {
        C00012() {
        }

        public void onReceive(Context context, Intent intent) {
            if (D2xxManager.ACTION_USB_PERMISSION.equals(intent.getAction())) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice) intent.getParcelableExtra("device");
                    if (!intent.getBooleanExtra("permission", false)) {
                        Log.d(D2xxManager.TAG, "permission denied for device " + device);
                    }
                }
            }
        }
    }

    public static class D2xxException extends IOException {
        private static final long serialVersionUID = 1;

        public D2xxException(String ftStatusMsg) {
            super(ftStatusMsg);
        }
    }

    public static class DriverParameters {
        private int mBufferSize;
        private int mMaxTransferSize;
        private int mNrBuffers;
        private int mRxTimeout;

        public DriverParameters() {
            this.mBufferSize = D2xxManager.FTDI_BREAK_ON;
            this.mMaxTransferSize = D2xxManager.FTDI_BREAK_ON;
            this.mNrBuffers = 16;
            this.mRxTimeout = 5000;
        }

        public boolean setMaxBufferSize(int size) {
            if (size < 64 || size > 262144) {
                Log.e(D2xxManager.TAG, "***bufferSize Out of correct range***");
                return false;
            }
            this.mBufferSize = size;
            return true;
        }

        public int getMaxBufferSize() {
            return this.mBufferSize;
        }

        public boolean setMaxTransferSize(int size) {
            if (size < 64 || size > 262144) {
                Log.e(D2xxManager.TAG, "***maxTransferSize Out of correct range***");
                return false;
            }
            this.mMaxTransferSize = size;
            return true;
        }

        public int getMaxTransferSize() {
            return this.mMaxTransferSize;
        }

        public boolean setBufferNumber(int number) {
            if (number < 2 || number > 16) {
                Log.e(D2xxManager.TAG, "***nrBuffers Out of correct range***");
                return false;
            }
            this.mNrBuffers = number;
            return true;
        }

        public int getBufferNumber() {
            return this.mNrBuffers;
        }

        public boolean setReadTimeout(int timeout) {
            this.mRxTimeout = timeout;
            return true;
        }

        public int getReadTimeout() {
            return this.mRxTimeout;
        }
    }

    public static class FtDeviceInfoListNode {
        public short bcdDevice;
        public int breakOnParam;
        public String description;
        public int flags;
        public int handle;
        public byte iSerialNumber;
        public int id;
        public short lineStatus;
        public int location;
        public short modemStatus;
        public String serialNumber;
        public int type;
    }

    static {
        mInstance = null;
        mContext = null;
        mPendingIntent = null;
        mPermissionFilter = null;
        mSupportedDevices = new ArrayList(Arrays.asList(new FtVidPid[]{new FtVidPid(1027, 24597), new FtVidPid(1027, 24596), new FtVidPid(1027, 24593), new FtVidPid(1027, 24592), new FtVidPid(1027, 24577), new FtVidPid(1027, 24582), new FtVidPid(1027, 24604), new FtVidPid(1027, 64193), new FtVidPid(1027, 64194), new FtVidPid(1027, 64195), new FtVidPid(1027, 64196), new FtVidPid(1027, 64197), new FtVidPid(1027, 64198), new FtVidPid(1027, 24594), new FtVidPid(2220, 4133), new FtVidPid(5590, FT_DEVICE_8U232AM), new FtVidPid(1027, 24599)}));
        mUsbDevicePermissions = new C00012();
    }

    private FT_Device findDevice(UsbDevice usbDev) {
        FT_Device rtDev = null;
        synchronized (this.mFtdiDevices) {
            int nr_dev = this.mFtdiDevices.size();
            for (int i = FT_DEVICE_232B; i < nr_dev; i += FT_DEVICE_8U232AM) {
                FT_Device ftDevice = (FT_Device) this.mFtdiDevices.get(i);
                if (ftDevice.getUsbDevice().equals(usbDev)) {
                    rtDev = ftDevice;
                    break;
                }
            }
        }
        return rtDev;
    }

    public boolean isFtDevice(UsbDevice dev) {
        boolean rc = false;
        if (mContext == null) {
            return Boolean.parseBoolean(String.valueOf(FT_DEVICE_232B));
        }
        FtVidPid vidPid = new FtVidPid(dev.getVendorId(), dev.getProductId());
        if (mSupportedDevices.contains(vidPid)) {
            rc = true;
        }
        Log.v(TAG, vidPid.toString());
        return rc;
    }

    private static synchronized boolean updateContext(Context context) {
        boolean rc = false;
        synchronized (D2xxManager.class) {
            if (context == null) {
                rc = false;
            } else {
                if (mContext != context) {
                    mContext = context;
                    mPendingIntent = PendingIntent.getBroadcast(mContext.getApplicationContext(), FT_DEVICE_232B, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_ONE_SHOT); //Wpisane, aby było. To było oryginalnie -> 134217728
                    mPermissionFilter = new IntentFilter(ACTION_USB_PERMISSION);
                    mContext.getApplicationContext().registerReceiver(mUsbDevicePermissions, mPermissionFilter);
                }
                rc = true;
            }
        }
        return rc;
    }

    private boolean isPermitted(UsbDevice dev) {
        if (!mUsbManager.hasPermission(dev)) {
            mUsbManager.requestPermission(dev, mPendingIntent);
        }
        if (mUsbManager.hasPermission(dev)) {
            return true;
        }
        return false;
    }

    private D2xxManager(Context parentContext) throws D2xxException {
        this.mUsbPlugEvents = new C00001();
        Log.v(TAG, "Start constructor");
        if (parentContext == null) {
            throw new D2xxException("D2xx init failed: Can not find parentContext!");
        }
        updateContext(parentContext);
        if (findUsbManger()) {
            this.mFtdiDevices = new ArrayList();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.hardware.usb.action.USB_DEVICE_ATTACHED");
            filter.addAction("android.hardware.usb.action.USB_DEVICE_DETACHED");
            parentContext.getApplicationContext().registerReceiver(this.mUsbPlugEvents, filter);
            Log.v(TAG, "End constructor");
            return;
        }
        throw new D2xxException("D2xx init failed: Can not find UsbManager!");
    }

    public static synchronized D2xxManager getInstance(Context parentContext) throws D2xxException {
        D2xxManager d2xxManager;
        synchronized (D2xxManager.class) {
            if (mInstance == null) {
                mInstance = new D2xxManager(parentContext);
            }
            if (parentContext != null) {
                updateContext(parentContext);
            }
            d2xxManager = mInstance;
        }
        return d2xxManager;
    }

    private static boolean findUsbManger() {
        if (mUsbManager == null && mContext != null) {
            mUsbManager = (UsbManager) mContext.getApplicationContext().getSystemService(Context.USB_SERVICE);
        }
        return mUsbManager != null;
    }

    public boolean setVIDPID(int vendorId, int productId) {
        boolean rc = false;
        if (vendorId == 0 || productId == 0) {
            Log.d(TAG, "Invalid parameter to setVIDPID");
        } else {
            FtVidPid vidpid = new FtVidPid(vendorId, productId);
            if (mSupportedDevices.contains(vidpid)) {
                Log.i(TAG, "Existing vid:" + vendorId + "  pid:" + productId);
                return true;
            } else if (mSupportedDevices.add(vidpid)) {
                rc = true;
            } else {
                Log.d(TAG, "Failed to add VID/PID combination to list.");
            }
        }
        return rc;
    }

    public int[][] getVIDPID() {
        int listSize = mSupportedDevices.size();
        int[][] arrayVIDPID = (int[][]) Array.newInstance(Integer.TYPE, new int[]{2, listSize});
        for (int i = FT_DEVICE_232B; i < listSize; i += FT_DEVICE_8U232AM) {
            FtVidPid vidpid = (FtVidPid) mSupportedDevices.get(i);
            arrayVIDPID[FT_DEVICE_232B][i] = vidpid.getVid();
            arrayVIDPID[FT_DEVICE_8U232AM][i] = vidpid.getPid();
        }
        return arrayVIDPID;
    }

    private void clearDevices() {
        synchronized (this.mFtdiDevices) {
            int nr_dev = this.mFtdiDevices.size();
            for (int i = FT_DEVICE_232B; i < nr_dev; i += FT_DEVICE_8U232AM) {
                this.mFtdiDevices.remove(FT_DEVICE_232B);
            }
        }
    }

    public int createDeviceInfoList(Context parentContext) {
        ArrayList<FT_Device> devices = new ArrayList();
        if (parentContext == null) {
            return FT_DEVICE_232B;
        }
        int rc;
        updateContext(parentContext);
        for (UsbDevice usbDevice : mUsbManager.getDeviceList().values()) {
            if (isFtDevice(usbDevice)) {
                int numInterfaces = usbDevice.getInterfaceCount();
                for (int i = FT_DEVICE_232B; i < numInterfaces; i += FT_DEVICE_8U232AM) {
                    if (isPermitted(usbDevice)) {
                        synchronized (this.mFtdiDevices) {
                            FT_Device ftDev = findDevice(usbDevice);
                            if (ftDev == null) {
                                ftDev = new FT_Device(parentContext, mUsbManager, usbDevice, usbDevice.getInterface(i));
                            } else {
                                this.mFtdiDevices.remove(ftDev);
                                ftDev.setContext(parentContext);
                            }
                            devices.add(ftDev);
                        }
                    }
                }
                continue;
            }
        }
        synchronized (this.mFtdiDevices) {
            clearDevices();
            this.mFtdiDevices = devices;
            rc = this.mFtdiDevices.size();
        }
        return rc;
    }

 /*   // JADX WARNING: inconsistent code.
      // Code decompiled incorrectly, please refer to instructions dump.
    public synchronized int getDeviceInfoList(int r3, com.jparzonka.mylibrary.j2xx.D2xxManager.FtDeviceInfoListNode[] r4) {

        r2 = this;
        monitor-enter(r2);
        r0 = 0;
    L_0x0002:
        if (r0 < r3) goto L_0x000c;
    L_0x0004:
        r1 = r2.mFtdiDevices;	 Catch:{ all -> 0x001b }
        r1 = r1.size();	 Catch:{ all -> 0x001b }
        monitor-exit(r2);
        return r1;
    L_0x000c:
        r1 = r2.mFtdiDevices;	 Catch:{ all -> 0x001b }
        r1 = r1.get(r0);	 Catch:{ all -> 0x001b }
        r1 = (com.ftdi.j2xx.FT_Device) r1;	 Catch:{ all -> 0x001b }
        r1 = r1.mDeviceInfoNode;	 Catch:{ all -> 0x001b }
        r4[r0] = r1;	 Catch:{ all -> 0x001b }
        r0 = r0 + 1;
        goto L_0x0002;
    L_0x001b:
        r1 = move-exception;
        monitor-exit(r2);
        throw r1;

        throw new UnsupportedOperationException("Method not decompiled: com.ftdi.j2xx.D2xxManager.getDeviceInfoList(int, com.ftdi.j2xx.D2xxManager$FtDeviceInfoListNode[]):int");
    }
  */

    public synchronized FtDeviceInfoListNode getDeviceInfoListDetail(int index) throws IndexOutOfBoundsException {
//        if (!mFtdiDevices.equals(null))
//            throw new NullPointerException("\nmFtdiDevices.size() -> " + mFtdiDevices.size()
//                    + "\nthis.mFtdiDevices.size() - > " + mFtdiDevices.size() + "\nindex -> " + index);
        if (mFtdiDevices.equals(null)) throw new NullPointerException("mFtdiDevices is null!");
        int x = 0;
        if (index == 1) x = 1;
        FtDeviceInfoListNode ftDeviceInfoListNode;
        if (index > this.mFtdiDevices.size() || index < 0) ftDeviceInfoListNode = null;
        else ftDeviceInfoListNode = this.mFtdiDevices.get(index - x).mDeviceInfoNode;

        return ftDeviceInfoListNode;
    }

    public static int getLibraryVersion() {
        return 543162368;
    }

    public boolean tryOpen(Context parentContext, FT_Device ftDev, DriverParameters params) {
        boolean rc = false;
        if (ftDev == null) {
            //  Toast.makeText(parentContext, "tryOpen passed ftDev == null", Toast.LENGTH_SHORT).show();
            return Boolean.getBoolean(String.valueOf(FT_DEVICE_232B));
        }
        if (parentContext == null) {
            //    Toast.makeText(parentContext, "tryOpen passed context == null", Toast.LENGTH_SHORT).show();
            return Boolean.getBoolean(String.valueOf(FT_DEVICE_232B));
        }
        ftDev.setContext(parentContext);
        if (params != null) {
            ftDev.setDriverParameters(params);
        }
        if (ftDev.openDevice(mUsbManager) && ftDev.isOpen()) {
            //         Toast.makeText(parentContext, "tryOpen ftDev.openDevice = TRUE", Toast.LENGTH_SHORT).show();
            rc = true;
        } else {
            //     Toast.makeText(parentContext, "tryOpen ftDev.openDevice = FALSE", Toast.LENGTH_SHORT).show();

        }
        return rc;
    }

    public synchronized FT_Device openByUsbDevice(Context parentContext, UsbDevice dev, DriverParameters params) {
        FT_Device ftDev;
        ftDev = null;
        if (isFtDevice(dev)) {
            ftDev = findDevice(dev);
            if (!tryOpen(parentContext, ftDev, params)) {
                Toast.makeText(parentContext, "openByUsbDevice tryOpen failed!", Toast.LENGTH_SHORT).show();
                ftDev = null;
            }
        }
        return ftDev;
    }

    public synchronized FT_Device openByUsbDevice(Context parentContext, UsbDevice dev) {
        return openByUsbDevice(parentContext, dev, null);
    }

    public synchronized FT_Device openByIndex(Context parentContext, int index, DriverParameters params) {
        FT_Device ftDev;
        if (index < 0) {
            ftDev = null;
        } else if (parentContext == null) {
            ftDev = null;
        } else {
            updateContext(parentContext);
            FT_Device ftDev2 = this.mFtdiDevices.get(index);
            if (!tryOpen(parentContext, ftDev2, params)) {
                // Toast.makeText(parentContext, "openByIndex tryOpen failed!", Toast.LENGTH_SHORT).show();
                ftDev2 = null;
            }
            ftDev = ftDev2;
        }
        return ftDev;
    }

    public synchronized FT_Device openByIndex(Context parentContext, int index) {
        return openByIndex(parentContext, index, null);
    }

    public synchronized FT_Device openBySerialNumber(Context parentContext, String serialNumber, DriverParameters params) {
        FT_Device ftDev;
        FT_Device ftDev2 = null;
        if (parentContext == null) {
            ftDev = null;
        } else {
            updateContext(parentContext);
            for (int i = FT_DEVICE_232B; i < this.mFtdiDevices.size(); i += FT_DEVICE_8U232AM) {
                FT_Device tmpDev = (FT_Device) this.mFtdiDevices.get(i);
                if (tmpDev != null) {
                    FtDeviceInfoListNode devInfo = tmpDev.mDeviceInfoNode;
                    if (devInfo == null) {
                        Log.d(TAG, "***devInfo cannot be null***");
                    } else if (devInfo.serialNumber.equals(serialNumber)) {
                        ftDev2 = tmpDev;
                        break;
                    }
                }
            }
            if (!tryOpen(parentContext, ftDev2, params)) {
                ftDev2 = null;
            }
            ftDev = ftDev2;
        }
        return ftDev;
    }

    public synchronized FT_Device openBySerialNumber(Context parentContext, String serialNumber) {
        return openBySerialNumber(parentContext, serialNumber, null);
    }

    public synchronized FT_Device openByDescription(Context parentContext, String description, DriverParameters params) {
        FT_Device ftDev;
        FT_Device ftDev2 = null;
        if (parentContext == null) {
            ftDev = null;
        } else {
            updateContext(parentContext);
            for (int i = FT_DEVICE_232B; i < this.mFtdiDevices.size(); i += FT_DEVICE_8U232AM) {
                FT_Device tmpDev = (FT_Device) this.mFtdiDevices.get(i);
                if (tmpDev != null) {
                    FtDeviceInfoListNode devInfo = tmpDev.mDeviceInfoNode;
                    if (devInfo == null) {
                        Log.d(TAG, "***devInfo cannot be null***");
                    } else if (devInfo.description.equals(description)) {
                        ftDev2 = tmpDev;
                        break;
                    }
                }
            }
            if (!tryOpen(parentContext, ftDev2, params)) {
                ftDev2 = null;
            }
            ftDev = ftDev2;
        }
        return ftDev;
    }

    public synchronized FT_Device openByDescription(Context parentContext, String description) {
        return openByDescription(parentContext, description, null);
    }

    public synchronized FT_Device openByLocation(Context parentContext, int location, DriverParameters params) {
        FT_Device ftDev;
        FT_Device ftDev2 = null;
        if (parentContext == null) {
            ftDev = null;
        } else {
            updateContext(parentContext);
            for (int i = FT_DEVICE_232B; i < this.mFtdiDevices.size(); i += FT_DEVICE_8U232AM) {
                FT_Device tmpDev = (FT_Device) this.mFtdiDevices.get(i);
                if (tmpDev != null) {
                    FtDeviceInfoListNode devInfo = tmpDev.mDeviceInfoNode;
                    if (devInfo == null) {
                        Log.d(TAG, "***devInfo cannot be null***");
                    } else if (devInfo.location == location) {
                        ftDev2 = tmpDev;
                        break;
                    }
                }
            }
            if (!tryOpen(parentContext, ftDev2, params)) {
                ftDev2 = null;
            }
            ftDev = ftDev2;
        }
        return ftDev;
    }

    public synchronized FT_Device openByLocation(Context parentContext, int location) {
        return openByLocation(parentContext, location, null);
    }

    public int addUsbDevice(UsbDevice dev) {
        int rc = FT_DEVICE_232B;
        if (isFtDevice(dev)) {
            int numInterfaces = dev.getInterfaceCount();
            for (int i = FT_DEVICE_232B; i < numInterfaces; i += FT_DEVICE_8U232AM) {
                if (isPermitted(dev)) {
                    synchronized (this.mFtdiDevices) {
                        FT_Device ftDev = findDevice(dev);
                        if (ftDev == null) {
                            ftDev = new FT_Device(mContext, mUsbManager, dev, dev.getInterface(i));
                        } else {
                            ftDev.setContext(mContext);
                            this.mFtdiDevices.remove(ftDev);
                        }
                        this.mFtdiDevices.add(ftDev);
                        rc += FT_DEVICE_8U232AM;
                    }
                }
            }
        }
        return rc;
    }
}
