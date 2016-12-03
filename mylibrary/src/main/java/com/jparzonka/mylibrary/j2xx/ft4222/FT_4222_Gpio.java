package com.jparzonka.mylibrary.j2xx.ft4222;

import android.util.Log;
import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.jparzonka.mylibrary.j2xx.interfaces.Gpio;
import com.jparzonka.mylibrary.j2xx.protocol.SpiSlaveResponseEvent;

public class FT_4222_Gpio implements Gpio {
    static final int GET_DIRECTION = 33;
    static final int GET_OPEN_DRAIN = 35;
    static final int GET_PULL_DOWN = 36;
    static final int GET_PULL_UP = 34;
    static final int GET_STATUS = 32;
    static final int SET_DIRECTION = 33;
    static final int SET_OPEN_DRAIN = 35;
    static final int SET_PULL_DOWN = 36;
    static final int SET_PULL_UP = 34;
    private static final int TOTAL_GPIOS = 4;
    private FT_4222_Device mFT4222Device;
    private FT_Device mFtDev;

    public FT_4222_Gpio(FT_4222_Device ft4222Device) {
        this.mFT4222Device = ft4222Device;
        this.mFtDev = this.mFT4222Device.mFtDev;
    }

    int cmdSet(int wValue1, int wValue2) {
        return this.mFtDev.VendorCmdSet(SET_DIRECTION, (wValue2 << 8) | wValue1);
    }

    int cmdSet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.mFtDev.VendorCmdSet(SET_DIRECTION, (wValue2 << 8) | wValue1, buf, datalen);
    }

    int cmdGet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.mFtDev.VendorCmdGet(GET_STATUS, (wValue2 << 8) | wValue1, buf, datalen);
    }

    public int init(int[] gpio) {
        chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
        gpio_dev gpioStatus = new gpio_dev(getFWVersion());
        byte[] data = new byte[1];
        gpio_mgr gpioMgr = new gpio_mgr();
        cmdSet(7, 0);
        cmdSet(6, 0);
        int ftStatus = this.mFT4222Device.init();
        if (ftStatus != 0) {
            Log.e("GPIO_M", "FT4222_GPIO init - 1 NG ftStatus:" + ftStatus);
            return ftStatus;
        } else if (chipStatus.chip_mode == 2 || chipStatus.chip_mode == 3) {
            return FT4222_STATUS.FT4222_GPIO_NOT_SUPPORTED_IN_THIS_MODE;
        } else {
            getStatus(gpioStatus);
            byte dir = gpioStatus.dir;
            data[0] = gpioStatus.dat[0];
            for (int idx = 0; idx < TOTAL_GPIOS; idx++) {
                if (gpio[idx] == 1) {
                    dir = (byte) (((1 << idx) | dir) & 15);
                } else {
                    dir = (byte) ((((1 << idx) ^ -1) & dir) & 15);
                }
            }
            gpioMgr.lastGpioData = data[0];
            cmdSet(SET_DIRECTION, dir);
            return 0;
        }
    }

    public int read(int portNum, boolean[] bValue) {
        gpio_dev gpioStatus = new gpio_dev(getFWVersion());
        int ftStatus = check(portNum);
        if (ftStatus != 0) {
            return ftStatus;
        }
        ftStatus = getStatus(gpioStatus);
        if (ftStatus != 0) {
            return ftStatus;
        }
        getGpioPinLevel(portNum, gpioStatus.dat[0], bValue);
        return 0;
    }

    public int write(int portNum, boolean bValue) {
        gpio_dev gpioStatus = new gpio_dev(getFWVersion());
        int ftStatus = check(portNum);
        if (ftStatus != 0) {
            return ftStatus;
        }
        if (!is_GPIOPort_Valid_Output(portNum)) {
            return FT4222_STATUS.FT4222_GPIO_WRITE_NOT_SUPPORTED;
        }
        getStatus(gpioStatus);
        byte[] bArr;
        if (bValue) {
            bArr = gpioStatus.dat;
            bArr[0] = (byte) (bArr[0] | (1 << portNum));
        } else {
            bArr = gpioStatus.dat;
            bArr[0] = (byte) (bArr[0] & (((1 << portNum) ^ -1) & 15));
        }
        if (this.mFtDev.write(gpioStatus.dat, 1) > 0) {
            return 0;
        }
        return 18;
    }

    int check(int portNum) {
        chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
        if (chipStatus.chip_mode == 2 || chipStatus.chip_mode == 3) {
            return FT4222_STATUS.FT4222_GPIO_NOT_SUPPORTED_IN_THIS_MODE;
        }
        if (portNum >= TOTAL_GPIOS) {
            return FT4222_STATUS.FT4222_GPIO_EXCEEDED_MAX_PORTNUM;
        }
        return 0;
    }

    int getStatus(gpio_dev gpioStatus) {
        byte[] buf;
        if (getFWVersion() < 'B') {
            buf = new byte[8];
        } else {
            buf = new byte[6];
        }
        int ftStatus = cmdGet(GET_STATUS, 0, buf, buf.length);
        gpioStatus.usb.ep_in = buf[0];
        gpioStatus.usb.ep_out = buf[1];
        gpioStatus.mask = buf[buf.length - 3];
        gpioStatus.dir = buf[buf.length - 2];
        gpioStatus.dat[0] = buf[buf.length - 1];
        if (ftStatus == buf.length) {
            return 0;
        }
        return ftStatus;
    }

    void getGpioPinLevel(int portNum, byte data, boolean[] value) {
        value[0] = IntToBool((((1 << portNum) & data) >> portNum) & 1);
    }

    boolean is_GPIOPort(int portNum) {
        chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
        boolean ret = true;
        switch (chipStatus.chip_mode) {
            case SpiSlaveResponseEvent.OK /*0*/:
                if ((portNum == 0 || portNum == 1) && (chipStatus.function == (byte) 1 || chipStatus.function == (byte) 2)) {
                    ret = false;
                }
                if (IntToBool(chipStatus.enable_suspend_out) && portNum == 2) {
                    ret = false;
                }
                if (IntToBool(chipStatus.enable_wakeup_int) && portNum == 3) {
                    return false;
                }
                return ret;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                if (portNum == 0 || portNum == 1) {
                    ret = false;
                }
                if (IntToBool(chipStatus.enable_suspend_out) && portNum == 2) {
                    ret = false;
                }
                if (IntToBool(chipStatus.enable_wakeup_int) && portNum == 3) {
                    return false;
                }
                return ret;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                return false;
            default:
                return true;
        }
    }

    boolean is_GPIOPort_Valid_Output(int portNum) {
        gpio_dev gpioStatus = new gpio_dev(getFWVersion());
        boolean ret = is_GPIOPort(portNum);
        getStatus(gpioStatus);
        if (!ret || ((gpioStatus.dir >> portNum) & 1) == 1) {
            return ret;
        }
        return false;
    }

    boolean is_GPIOPort_Valid_Input(int portNum) {
        gpio_dev gpioStatus = new gpio_dev(getFWVersion());
        boolean ret = is_GPIOPort(portNum);
        getStatus(gpioStatus);
        if (!ret || ((gpioStatus.dir >> portNum) & 1) == 0) {
            return ret;
        }
        return false;
    }

    boolean update_GPIO_Status(int portNum, int gpioStatus) {
        gpio_mgr gpio = new gpio_mgr();
        if (gpio.gpioStatus[portNum] == gpioStatus) {
            return true;
        }
        char pullup = '\u0000';
        char pulldown = '\u0000';
        char opendrain = '\u0000';
        gpio.gpioStatus[portNum] = gpioStatus;
        for (int idx = 0; idx < TOTAL_GPIOS; idx++) {
            switch (gpio.gpioStatus[idx]) {
                case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                    pullup = (char) ((1 << idx) + pullup);
                    break;
                case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                    pulldown = (char) ((1 << idx) + pulldown);
                    break;
                case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                    opendrain = (char) ((1 << idx) + opendrain);
                    break;
                default:
                    break;
            }
        }
        int ftStatus = (cmdSet(SET_PULL_UP, pullup) | cmdSet(SET_PULL_DOWN, pulldown)) | cmdSet(SET_OPEN_DRAIN, opendrain);
        if (ftStatus == 0) {
            gpio.gpioStatus[portNum] = gpioStatus;
        }
        if (ftStatus == 0) {
            return true;
        }
        return false;
    }

    boolean IntToBool(int i) {
        return i != 0;
    }

    char getFWVersion() {
        return this.mFT4222Device.GetVersion();
    }
}
