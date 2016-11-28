package com.jparzonka.mylibrary.j2xx;

import android.util.Log;
import com.jparzonka.mylibrary.j2xx.D2xxManager.D2xxException;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.CHIPTOP_CMD;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.I2C_CMD;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.SPI_CMD;

class FT_EE_Ctrl {
    private static final int BUS_POWERED = 128;
    private static final short EE_MAX_SIZE = (short) 1024;
    private static final int ENABLE_SERIAL_NUMBER = 8;
    private static final int PULL_DOWN_IN_USB_SUSPEND = 4;
    private static final int SELF_POWERED = 64;
    private static final int USB_REMOTE_WAKEUP = 32;
    private FT_Device mDevice;
    boolean mEepromBlank;
    int mEepromSize;
    short mEepromType;

    static final class EepromType {
        static final short INVALID = (short) 255;
        static final short TYPE_46 = (short) 70;
        static final short TYPE_52 = (short) 82;
        static final short TYPE_56 = (short) 86;
        static final short TYPE_66 = (short) 102;
        static final short TYPE_MTP = (short) 1;

        EepromType() {
        }
    }

    FT_EE_Ctrl(FT_Device dev) {
        this.mDevice = dev;
    }

    int readWord(short offset) {
        byte[] dataRead = new byte[2];
        if (offset >= EE_MAX_SIZE) {
            return -1;
        }
        this.mDevice.getConnection().controlTransfer(-64, 144, 0, offset, dataRead, 2, 0);
        return ((dataRead[1] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) << ENABLE_SERIAL_NUMBER) | (dataRead[0] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
    }

    boolean writeWord(short offset, short value) {
        int wValue = value & 65535;
        int wIndex = offset & 65535;
        boolean rc = false;
        if (offset >= EE_MAX_SIZE) {
            return Boolean.valueOf(String.valueOf(0));
        }
        if (this.mDevice.getConnection().controlTransfer(SELF_POWERED, 145, wValue, wIndex, null, 0, 0) == 0) {
            rc = true;
        }
        return rc;
    }

    int eraseEeprom() {
        return this.mDevice.getConnection().controlTransfer(SELF_POWERED, 146, 0, 0, null, 0, 0);
    }

    short programEeprom(FT_EEPROM eeprom) {
        return (short) 1;
    }

    boolean programEeprom(int[] dataToWrite, int ee_size) {
        int checksumLocation = ee_size;
        int Checksum = 43690;
        int addressCounter = 0;
        while (addressCounter < checksumLocation) {
            writeWord((short) addressCounter, (short) dataToWrite[addressCounter]);
            int TempChecksum = (dataToWrite[addressCounter] ^ Checksum) & 65535;
            Checksum = (((short) ((TempChecksum << 1) & 65535)) | ((short) ((TempChecksum >> 15) & 65535))) & 65535;
            addressCounter++;
            Log.d("FT_EE_Ctrl", "Entered WriteWord Checksum : " + Checksum);
        }
        writeWord((short) checksumLocation, (short) Checksum);
        return true;
    }

    FT_EEPROM readEeprom() {
        return null;
    }

    int setUSBConfig(Object ee) {
        FT_EEPROM ft = (FT_EEPROM) ee;
        int lowerbits = 0 | BUS_POWERED;
        if (ft.RemoteWakeup) {
            lowerbits |= USB_REMOTE_WAKEUP;
        }
        if (ft.SelfPowered) {
            lowerbits |= SELF_POWERED;
        }
        return ((ft.MaxPower / 2) << ENABLE_SERIAL_NUMBER) | lowerbits;
    }

    void getUSBConfig(FT_EEPROM ee, int dataRead) {
        ee.MaxPower = (short) (((byte) (dataRead >> ENABLE_SERIAL_NUMBER)) * 2);
        byte P = (byte) dataRead;
        if ((P & SELF_POWERED) == SELF_POWERED && (P & BUS_POWERED) == BUS_POWERED) {
            ee.SelfPowered = true;
        } else {
            ee.SelfPowered = false;
        }
        if ((P & USB_REMOTE_WAKEUP) == USB_REMOTE_WAKEUP) {
            ee.RemoteWakeup = true;
        } else {
            ee.RemoteWakeup = false;
        }
    }

    int setDeviceControl(Object ee) {
        int data;
        FT_EEPROM ft = (FT_EEPROM) ee;
        if (ft.PullDownEnable) {
            data = 0 | PULL_DOWN_IN_USB_SUSPEND;
        } else {
            data = 0 & 251;
        }
        if (ft.SerNumEnable) {
            return data | ENABLE_SERIAL_NUMBER;
        }
        return data & 247;
    }

    void getDeviceControl(Object ee, int dataRead) {
        FT_EEPROM ft = (FT_EEPROM) ee;
        if ((dataRead & PULL_DOWN_IN_USB_SUSPEND) > 0) {
            ft.PullDownEnable = true;
        } else {
            ft.PullDownEnable = false;
        }
        if ((dataRead & ENABLE_SERIAL_NUMBER) > 0) {
            ft.SerNumEnable = true;
        } else {
            ft.SerNumEnable = false;
        }
    }

    int setStringDescriptor(String s, int[] data, int addrs, int pointer, boolean rdevice) {
        int i = 0;
        int strLength = (s.length() * 2) + 2;
        data[pointer] = (strLength << ENABLE_SERIAL_NUMBER) | (addrs * 2);
        if (rdevice) {
            data[pointer] = data[pointer] + BUS_POWERED;
        }
        char[] strchar = s.toCharArray();
        int addrs2 = addrs + 1;
        data[addrs] = strLength | 768;
        strLength = (strLength - 2) / 2;
        addrs = addrs2;
        while (true) {
            addrs2 = addrs + 1;
            data[addrs] = strchar[i];
            i++;
            if (i >= strLength) {
                return addrs2;
            }
            addrs = addrs2;
        }
    }

    String getStringDescriptor(int addr, int[] dataRead) {
        String descriptor = "";
        addr++;
        int endaddr = addr + (((dataRead[addr] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2) - 1);
        for (int i = addr; i < endaddr; i++) {
            descriptor = new StringBuilder(String.valueOf(descriptor)).append((char) dataRead[i]).toString();
        }
        return descriptor;
    }

    void clearUserDataArea(int saddr, int eeprom_size, int[] data) {
        int i = saddr;
        while (i < eeprom_size) {
            saddr = i + 1;
            data[i] = 0;
            i = saddr;
        }
    }

    int getEepromSize(byte location) throws D2xxException {
        int[] dataRead = new int[3];
        int eeData = (short) readWord((short) (location & -1));
        if (eeData != 65535) {
            switch (eeData) {
                case SPI_CMD.SPI_SET_CPHA /*70*/:
                    this.mEepromType = (short) 70;
                    this.mEepromSize = SELF_POWERED;
                    this.mEepromBlank = false;
                    return SELF_POWERED;
                case I2C_CMD.I2C_MASTER_SET_I2CMTP /*82*/:
                    this.mEepromType = (short) 82;
                    this.mEepromSize = 1024;
                    this.mEepromBlank = false;
                    return 1024;
                case 86:
                    this.mEepromType = (short) 86;
                    this.mEepromSize = BUS_POWERED;
                    this.mEepromBlank = false;
                    return BUS_POWERED;
                case 102:
                    this.mEepromType = (short) 102;
                    this.mEepromSize = BUS_POWERED;
                    this.mEepromBlank = false;
                    return 256;
                default:
                    return 0;
            }
        }
        boolean rc = writeWord((short) 192, (short) CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE);
        dataRead[0] = readWord((short) 192);
        dataRead[1] = readWord((short) 64);
        dataRead[2] = readWord((short) 0);
        if (rc) {
            this.mEepromBlank = true;
            if ((readWord((short) 0) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) == CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE) {
                eraseEeprom();
                this.mEepromType = (short) 70;
                this.mEepromSize = SELF_POWERED;
                return SELF_POWERED;
            } else if ((readWord((short) 64) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) == CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE) {
                eraseEeprom();
                this.mEepromType = (short) 86;
                this.mEepromSize = BUS_POWERED;
                return BUS_POWERED;
            } else if ((readWord((short) 192) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) == CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE) {
                eraseEeprom();
                this.mEepromType = (short) 102;
                this.mEepromSize = BUS_POWERED;
                return 256;
            } else {
                eraseEeprom();
                return 0;
            }
        }
        this.mEepromType = (short) 255;
        this.mEepromSize = 0;
        return 0;
    }

    int writeUserData(byte[] data) {
        return 0;
    }

    byte[] readUserData(int length) {
        return null;
    }

    int getUserSize() {
        return 0;
    }
}
