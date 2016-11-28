/*
package com.jparzonka.mylibrary.j2xx;

import com.jparzonka.mylibrary.j2xx.D2xxManager.D2xxException;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;
import com.jparzonka.mylibrary.j2xx.protocol.SpiSlaveResponseEvent;

class FT_EE_232H_Ctrl extends FT_EE_Ctrl {
    private static final int AL_DRIVE_CURRENT = 3;
    private static final int AL_FAST_SLEW = 4;
    private static final int AL_SCHMITT_INPUT = 8;
    private static final int BL_DRIVE_CURRENT = 768;
    private static final int BL_FAST_SLEW = 1024;
    private static final int BL_SCHMITT_INPUT = 2048;
    private static final String DEFAULT_PID = "6014";
    private static final byte EEPROM_SIZE_LOCATION = (byte) 15;
    private static FT_Device ft_device;

    FT_EE_232H_Ctrl(FT_Device usbc) throws D2xxException {
        super(usbc);
        getEepromSize(EEPROM_SIZE_LOCATION);
    }

    short programEeprom(FT_EEPROM ee) {
        int[] dataToWrite = new int[this.mEepromSize];
        if (ee.getClass() != FT_EEPROM_232H.class) {
            return (short) 1;
        }
        FT_EEPROM_232H eeprom = (FT_EEPROM_232H) ee;
        try {
            if (eeprom.FIFO) {
                dataToWrite[0] = dataToWrite[0] | 1;
            } else if (eeprom.FIFOTarget) {
                dataToWrite[0] = dataToWrite[0] | 2;
            } else if (eeprom.FastSerial) {
                dataToWrite[0] = dataToWrite[0] | AL_FAST_SLEW;
            }
            if (eeprom.FT1248) {
                dataToWrite[0] = dataToWrite[0] | AL_SCHMITT_INPUT;
            }
            if (eeprom.LoadVCP) {
                dataToWrite[0] = dataToWrite[0] | 16;
            }
            if (eeprom.FT1248ClockPolarity) {
                dataToWrite[0] = dataToWrite[0] | 256;
            }
            if (eeprom.FT1248LSB) {
                dataToWrite[0] = dataToWrite[0] | 512;
            }
            if (eeprom.FT1248FlowControl) {
                dataToWrite[0] = dataToWrite[0] | BL_FAST_SLEW;
            }
            if (eeprom.PowerSaveEnable) {
                dataToWrite[0] = dataToWrite[0] | 32768;
            }
            dataToWrite[1] = eeprom.VendorId;
            dataToWrite[2] = eeprom.ProductId;
            dataToWrite[AL_DRIVE_CURRENT] = 2304;
            dataToWrite[AL_FAST_SLEW] = setUSBConfig(ee);
            dataToWrite[5] = setDeviceControl(ee);
            short driveA = eeprom.AL_DriveCurrent;
            if (driveA == -1) {
                driveA = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | driveA;
            if (eeprom.AL_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | AL_FAST_SLEW;
            }
            if (eeprom.AL_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | AL_SCHMITT_INPUT;
            }
            short driveC = eeprom.BL_DriveCurrent;
            if (driveC == -1) {
                driveC = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | ((short) (driveC << AL_SCHMITT_INPUT));
            if (eeprom.BL_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | BL_FAST_SLEW;
            }
            if (eeprom.BL_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | BL_SCHMITT_INPUT;
            }
            int offset = setStringDescriptor(eeprom.Product, dataToWrite, setStringDescriptor(eeprom.Manufacturer, dataToWrite, 80, 7, false), AL_SCHMITT_INPUT, false);
            if (eeprom.SerNumEnable) {
                offset = setStringDescriptor(eeprom.SerialNumber, dataToWrite, offset, 9, false);
            }
            dataToWrite[10] = 0;
            dataToWrite[11] = 0;
            dataToWrite[12] = 0;
            int c2 = eeprom.CBus2 << AL_SCHMITT_INPUT;
            dataToWrite[12] = ((eeprom.CBus0 | (eeprom.CBus1 << AL_FAST_SLEW)) | c2) | (eeprom.CBus3 << 12);
            dataToWrite[13] = 0;
            int c6 = eeprom.CBus6 << AL_SCHMITT_INPUT;
            dataToWrite[13] = ((eeprom.CBus4 | (eeprom.CBus5 << AL_FAST_SLEW)) | c6) | (eeprom.CBus7 << 12);
            dataToWrite[14] = 0;
            dataToWrite[14] = eeprom.CBus8 | (eeprom.CBus9 << AL_FAST_SLEW);
            dataToWrite[15] = this.mEepromType;
            dataToWrite[69] = 72;
            if (this.mEepromType == (short) 70) {
                return (short) 1;
            }
            if (dataToWrite[1] == 0 || dataToWrite[2] == 0) {
                return (short) 2;
            }
            if (programEeprom(dataToWrite, this.mEepromSize - 1)) {
                return (short) 0;
            }
            return (short) 1;
        } catch (Exception e) {
            e.printStackTrace();
            return (short) 0;
        }
    }

    FT_EEPROM readEeprom() {
        FT_EEPROM eeprom = new FT_EEPROM_232H();
        int[] data = new int[this.mEepromSize];
        if (this.mEepromBlank) {
            return eeprom;
        }
        short i = (short) 0;
        while (true) {
            try {
                if (i >= this.mEepromSize) {
                    break;
                }
                data[i] = readWord(i);
                i = (short) (i + 1);
            } catch (Exception e) {
                return null;
            }
        }
        eeprom.UART = false;
        switch (data[0] & 15) {
            case SpiSlaveResponseEvent.OK */
/*0*//*
:
                eeprom.UART = true;
                break;
            case SpiSlaveResponseEvent.DATA_CORRUPTED */
/*1*//*
:
                eeprom.FIFO = true;
                break;
            case SpiSlaveResponseEvent.IO_ERROR */
/*2*//*
:
                eeprom.FIFOTarget = true;
                break;
            case AL_FAST_SLEW */
/*4*//*
:
                eeprom.FastSerial = true;
                break;
            case AL_SCHMITT_INPUT */
/*8*//*
:
                eeprom.FT1248 = true;
                break;
            default:
                eeprom.UART = true;
                break;
        }
        if ((data[0] & 16) > 0) {
            eeprom.LoadVCP = true;
            eeprom.LoadD2XX = false;
        } else {
            eeprom.LoadVCP = false;
            eeprom.LoadD2XX = true;
        }
        if ((data[0] & 256) > 0) {
            eeprom.FT1248ClockPolarity = true;
        } else {
            eeprom.FT1248ClockPolarity = false;
        }
        if ((data[0] & 512) > 0) {
            eeprom.FT1248LSB = true;
        } else {
            eeprom.FT1248LSB = false;
        }
        if ((data[0] & BL_FAST_SLEW) > 0) {
            eeprom.FT1248FlowControl = true;
        } else {
            eeprom.FT1248FlowControl = false;
        }
        if ((data[0] & 32768) > 0) {
            eeprom.PowerSaveEnable = true;
        }
        eeprom.VendorId = (short) data[1];
        eeprom.ProductId = (short) data[2];
        getUSBConfig(eeprom, data[AL_FAST_SLEW]);
        getDeviceControl(eeprom, data[5]);
        switch (data[6] & AL_DRIVE_CURRENT) {
            case SpiSlaveResponseEvent.OK */
/*0*//*
:
                eeprom.AL_DriveCurrent = (byte) 0;
                break;
            case SpiSlaveResponseEvent.DATA_CORRUPTED */
/*1*//*
:
                eeprom.AL_DriveCurrent = (byte) 1;
                break;
            case SpiSlaveResponseEvent.IO_ERROR */
/*2*//*
:
                eeprom.AL_DriveCurrent = (byte) 2;
                break;
            case AL_DRIVE_CURRENT */
/*3*//*
:
                eeprom.AL_DriveCurrent = (byte) 3;
                break;
        }
        if ((data[6] & AL_FAST_SLEW) > 0) {
            eeprom.AL_SlowSlew = true;
        } else {
            eeprom.AL_SlowSlew = false;
        }
        if ((data[6] & AL_SCHMITT_INPUT) > 0) {
            eeprom.AL_SchmittInput = true;
        } else {
            eeprom.AL_SchmittInput = false;
        }
        switch ((short) ((data[6] & BL_DRIVE_CURRENT) >> AL_SCHMITT_INPUT)) {
            case SpiSlaveResponseEvent.OK */
/*0*//*
:
                eeprom.BL_DriveCurrent = (byte) 0;
                break;
            case SpiSlaveResponseEvent.DATA_CORRUPTED */
/*1*//*
:
                eeprom.BL_DriveCurrent = (byte) 1;
                break;
            case SpiSlaveResponseEvent.IO_ERROR */
/*2*//*
:
                eeprom.BL_DriveCurrent = (byte) 2;
                break;
            case AL_DRIVE_CURRENT */
/*3*//*
:
                eeprom.BL_DriveCurrent = (byte) 3;
                break;
        }
        if ((data[6] & BL_FAST_SLEW) > 0) {
            eeprom.BL_SlowSlew = true;
        } else {
            eeprom.BL_SlowSlew = false;
        }
        if ((data[6] & BL_SCHMITT_INPUT) > 0) {
            eeprom.BL_SchmittInput = true;
        } else {
            eeprom.BL_SchmittInput = false;
        }
        eeprom.CBus0 = (byte) ((short) ((data[12] >> 0) & 15));
        eeprom.CBus1 = (byte) ((short) ((data[12] >> AL_FAST_SLEW) & 15));
        eeprom.CBus2 = (byte) ((short) ((data[12] >> AL_SCHMITT_INPUT) & 15));
        eeprom.CBus3 = (byte) ((short) ((data[12] >> 12) & 15));
        eeprom.CBus4 = (byte) ((short) ((data[13] >> 0) & 15));
        eeprom.CBus5 = (byte) ((short) ((data[13] >> AL_FAST_SLEW) & 15));
        eeprom.CBus6 = (byte) ((short) ((data[13] >> AL_SCHMITT_INPUT) & 15));
        eeprom.CBus7 = (byte) ((short) ((data[13] >> 12) & 15));
        eeprom.CBus8 = (byte) ((short) ((data[14] >> 0) & 15));
        eeprom.CBus9 = (byte) ((short) ((data[14] >> AL_FAST_SLEW) & 15));
        eeprom.Manufacturer = getStringDescriptor((data[7] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2, data);
        eeprom.Product = getStringDescriptor((data[AL_SCHMITT_INPUT] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2, data);
        eeprom.SerialNumber = getStringDescriptor((data[9] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2, data);
        return eeprom;
    }

    int getUserSize() {
        int data = readWord((short) 9);
        return (((this.mEepromSize - (((data & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2) + 1)) - 1) - ((((65280 & data) >> AL_SCHMITT_INPUT) / 2) + 1)) * 2;
    }

    int writeUserData(byte[] data) {
        if (data.length > getUserSize()) {
            return 0;
        }
        int i;
        int[] eeprom = new int[this.mEepromSize];
        for (i = 0; i < this.mEepromSize; i = (short) (i + 1)) {
            eeprom[i] = readWord(i);
        }
        short offset = (short) (((this.mEepromSize - (getUserSize() / 2)) - 1) - 1);
        i = 0;
        while (i < data.length) {
            int dataWrite;
            if (i + 1 < data.length) {
                dataWrite = data[i + 1] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            } else {
                dataWrite = 0;
            }
            short offset2 = (short) (offset + 1);
            eeprom[offset] = (dataWrite << AL_SCHMITT_INPUT) | (data[i] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            i += 2;
            offset = offset2;
        }
        if (eeprom[1] == 0 || eeprom[2] == 0 || !programEeprom(eeprom, this.mEepromSize - 1)) {
            return 0;
        }
        return data.length;
    }

    byte[] readUserData(int length) {
        byte[] data = new byte[length];
        if (length == 0 || length > getUserSize()) {
            return null;
        }
        int i = 0;
        short offset = (short) (((this.mEepromSize - (getUserSize() / 2)) - 1) - 1);
        while (i < length) {
            short offset2 = (short) (offset + 1);
            int dataRead = readWord(offset);
            if (i + 1 < data.length) {
                data[i + 1] = (byte) (dataRead & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            }
            data[i] = (byte) ((65280 & dataRead) >> AL_SCHMITT_INPUT);
            i += 2;
            offset = offset2;
        }
        return data;
    }
}
*/
