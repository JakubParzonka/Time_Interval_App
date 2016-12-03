/*
package com.jparzonka.mylibrary.j2xx;

import com.jparzonka.mylibrary.j2xx.D2xxManager.D2xxException;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;
import com.jparzonka.mylibrary.j2xx.protocol.SpiSlaveResponseEvent;

class FT_EE_4232H_Ctrl extends FT_EE_Ctrl {
    private static final int AH_DRIVE_CURRENT = 48;
    private static final int AH_FAST_SLEW = 64;
    private static final int AH_SCHMITT_INPUT = 128;
    private static final int AH_TXDEN = 8192;
    private static final int AL_DRIVE_CURRENT = 3;
    private static final int AL_FAST_SLEW = 4;
    private static final int AL_SCHMITT_INPUT = 8;
    private static final int AL_TXDEN = 4096;
    private static final int BH_DRIVE_CURRENT = 12288;
    private static final int BH_FAST_SLEW = 16384;
    private static final int BH_SCHMITT_INPUT = 32768;
    private static final int BH_TXDEN = 32768;
    private static final int BL_DRIVE_CURRENT = 768;
    private static final int BL_FAST_SLEW = 1024;
    private static final int BL_SCHMITT_INPUT = 2048;
    private static final int BL_TXDEN = 16384;
    private static final String DEFAULT_PID = "6011";
    private static final byte EEPROM_SIZE_LOCATION = (byte) 12;
    private static final int TPRDRV = 24;

    FT_EE_4232H_Ctrl(FT_Device usbC) throws D2xxException {
        super(usbC);
        getEepromSize(EEPROM_SIZE_LOCATION);
    }

    short programEeprom(FT_EEPROM ee) {
        int[] dataToWrite = new int[this.mEepromSize];
        if (ee.getClass() != FT_EEPROM_4232H.class) {
            return (short) 1;
        }
        FT_EEPROM_4232H eeprom = (FT_EEPROM_4232H) ee;
        try {
            dataToWrite[0] = 0;
            if (eeprom.AL_LoadVCP) {
                dataToWrite[0] = dataToWrite[0] | AL_SCHMITT_INPUT;
            }
            if (eeprom.BL_LoadVCP) {
                dataToWrite[0] = dataToWrite[0] | AH_SCHMITT_INPUT;
            }
            if (eeprom.AH_LoadVCP) {
                dataToWrite[0] = dataToWrite[0] | BL_SCHMITT_INPUT;
            }
            if (eeprom.BH_LoadVCP) {
                dataToWrite[0] = dataToWrite[0] | BH_TXDEN;
            }
            dataToWrite[1] = eeprom.VendorId;
            dataToWrite[2] = eeprom.ProductId;
            dataToWrite[AL_DRIVE_CURRENT] = BL_SCHMITT_INPUT;
            dataToWrite[AL_FAST_SLEW] = setUSBConfig(ee);
            dataToWrite[5] = setDeviceControl(ee);
            if (eeprom.AL_LoadRI_RS485) {
                dataToWrite[5] = (short) (dataToWrite[5] | AL_TXDEN);
            }
            if (eeprom.AH_LoadRI_RS485) {
                dataToWrite[5] = (short) (dataToWrite[5] | AH_TXDEN);
            }
            if (eeprom.BL_LoadRI_RS485) {
                dataToWrite[5] = (short) (dataToWrite[5] | BL_TXDEN);
            }
            if (eeprom.BH_LoadRI_RS485) {
                dataToWrite[5] = (short) (dataToWrite[5] | BH_TXDEN);
            }
            dataToWrite[6] = 0;
            short driveA = eeprom.AL_DriveCurrent;
            if (driveA == (short) -1) {
                driveA = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | driveA;
            if (eeprom.AL_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | AL_FAST_SLEW;
            }
            if (eeprom.AL_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | AL_SCHMITT_INPUT;
            }
            short driveB = eeprom.AH_DriveCurrent;
            if (driveB == (short) -1) {
                driveB = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | ((short) (driveB << AL_FAST_SLEW));
            if (eeprom.AH_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | AH_FAST_SLEW;
            }
            if (eeprom.AH_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | AH_SCHMITT_INPUT;
            }
            short driveC = eeprom.BL_DriveCurrent;
            if (driveC == (short) -1) {
                driveC = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | ((short) (driveC << AL_SCHMITT_INPUT));
            if (eeprom.BL_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | BL_FAST_SLEW;
            }
            if (eeprom.BL_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | BL_SCHMITT_INPUT;
            }
            dataToWrite[6] = dataToWrite[6] | ((short) (eeprom.BH_DriveCurrent << 12));
            if (eeprom.BH_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | BL_TXDEN;
            }
            if (eeprom.BH_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | BH_TXDEN;
            }
            boolean eeprom46 = false;
            int offset = 77;
            if (this.mEepromType == (short) 70) {
                offset = 13;
                eeprom46 = true;
            }
            offset = setStringDescriptor(eeprom.Product, dataToWrite, setStringDescriptor(eeprom.Manufacturer, dataToWrite, offset, 7, eeprom46), AL_SCHMITT_INPUT, eeprom46);
            if (eeprom.SerNumEnable) {
                offset = setStringDescriptor(eeprom.SerialNumber, dataToWrite, offset, 9, eeprom46);
            }
            switch (eeprom.TPRDRV) {
                case SpiSlaveResponseEvent.OK */
/*0*//*
:
                    dataToWrite[11] = 0;
                    break;
                case SpiSlaveResponseEvent.DATA_CORRUPTED */
/*1*//*
:
                    dataToWrite[11] = AL_SCHMITT_INPUT;
                    break;
                case SpiSlaveResponseEvent.IO_ERROR */
/*2*//*
:
                    dataToWrite[11] = 16;
                    break;
                case AL_DRIVE_CURRENT */
/*3*//*
:
                    dataToWrite[11] = TPRDRV;
                    break;
                default:
                    dataToWrite[11] = 0;
                    break;
            }
            dataToWrite[12] = this.mEepromType;
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
        FT_EEPROM eeprom = new FT_EEPROM_4232H();
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
        if (((short) ((data[0] & AL_SCHMITT_INPUT) >> AL_DRIVE_CURRENT)) == (short) 1) {
            eeprom.AL_LoadVCP = true;
            eeprom.AL_LoadD2XX = false;
        } else {
            eeprom.AL_LoadVCP = false;
            eeprom.AL_LoadD2XX = true;
        }
        short data7x00 = (short) ((data[0] & AH_SCHMITT_INPUT) >> 7);
        if (r0 == (short) 1) {
            eeprom.BL_LoadVCP = true;
            eeprom.BL_LoadD2XX = false;
        } else {
            eeprom.BL_LoadVCP = false;
            eeprom.BL_LoadD2XX = true;
        }
        if (((short) ((data[0] & BL_SCHMITT_INPUT) >> 11)) == (short) 1) {
            eeprom.AH_LoadVCP = true;
            eeprom.AH_LoadD2XX = false;
        } else {
            eeprom.AH_LoadVCP = false;
            eeprom.AH_LoadD2XX = true;
        }
        if (((short) ((data[0] & BH_TXDEN) >> 15)) == (short) 1) {
            eeprom.BH_LoadVCP = true;
            eeprom.BH_LoadD2XX = false;
        } else {
            eeprom.BH_LoadVCP = false;
            eeprom.BH_LoadD2XX = true;
        }
        eeprom.VendorId = (short) data[1];
        eeprom.ProductId = (short) data[2];
        getUSBConfig(eeprom, data[AL_FAST_SLEW]);
        getDeviceControl(eeprom, data[5]);
        int i2 = data[5] & AL_TXDEN;
        if (r0 == AL_TXDEN) {
            eeprom.AL_LoadRI_RS485 = true;
        }
        i2 = data[5] & AH_TXDEN;
        if (r0 == AH_TXDEN) {
            eeprom.AH_LoadRI_RS485 = true;
        }
        i2 = data[5] & BL_TXDEN;
        if (r0 == BL_TXDEN) {
            eeprom.AH_LoadRI_RS485 = true;
        }
        if ((data[5] & BH_TXDEN) == BH_TXDEN) {
            eeprom.AH_LoadRI_RS485 = true;
        }
        switch ((short) (data[6] & AL_DRIVE_CURRENT)) {
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
        if (((short) (data[6] & AL_FAST_SLEW)) == AL_FAST_SLEW) {
            eeprom.AL_SlowSlew = true;
        } else {
            eeprom.AL_SlowSlew = false;
        }
        if (((short) (data[6] & AL_SCHMITT_INPUT)) == AL_SCHMITT_INPUT) {
            eeprom.AL_SchmittInput = true;
        } else {
            eeprom.AL_SchmittInput = false;
        }
        switch ((short) ((data[6] & AH_DRIVE_CURRENT) >> AL_FAST_SLEW)) {
            case SpiSlaveResponseEvent.OK */
/*0*//*
:
                eeprom.AH_DriveCurrent = (byte) 0;
                break;
            case SpiSlaveResponseEvent.DATA_CORRUPTED */
/*1*//*
:
                eeprom.AH_DriveCurrent = (byte) 1;
                break;
            case SpiSlaveResponseEvent.IO_ERROR */
/*2*//*
:
                eeprom.AH_DriveCurrent = (byte) 2;
                break;
            case AL_DRIVE_CURRENT */
/*3*//*
:
                eeprom.AH_DriveCurrent = (byte) 3;
                break;
        }
        short data6x06 = (short) (data[6] & AH_FAST_SLEW);
        if (r0 == AH_FAST_SLEW) {
            eeprom.AH_SlowSlew = true;
        } else {
            eeprom.AH_SlowSlew = false;
        }
        short data7x06 = (short) (data[6] & AH_SCHMITT_INPUT);
        if (r0 == AH_SCHMITT_INPUT) {
            eeprom.AH_SchmittInput = true;
        } else {
            eeprom.AH_SchmittInput = false;
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
        if (((short) (data[6] & BL_FAST_SLEW)) == BL_FAST_SLEW) {
            eeprom.BL_SlowSlew = true;
        } else {
            eeprom.BL_SlowSlew = false;
        }
        if (((short) (data[6] & BL_SCHMITT_INPUT)) == BL_SCHMITT_INPUT) {
            eeprom.BL_SchmittInput = true;
        } else {
            eeprom.BL_SchmittInput = false;
        }
        switch ((short) ((data[6] & BH_DRIVE_CURRENT) >> 12)) {
            case SpiSlaveResponseEvent.OK */
/*0*//*
:
                eeprom.BH_DriveCurrent = (byte) 0;
                break;
            case SpiSlaveResponseEvent.DATA_CORRUPTED */
/*1*//*
:
                eeprom.BH_DriveCurrent = (byte) 1;
                break;
            case SpiSlaveResponseEvent.IO_ERROR */
/*2*//*
:
                eeprom.BH_DriveCurrent = (byte) 2;
                break;
            case AL_DRIVE_CURRENT */
/*3*//*
:
                eeprom.BH_DriveCurrent = (byte) 3;
                break;
        }
        if (((short) (data[6] & BL_TXDEN)) == BL_TXDEN) {
            eeprom.BH_SlowSlew = true;
        } else {
            eeprom.BH_SlowSlew = false;
        }
        if (((short) (data[6] & BH_TXDEN)) == BH_TXDEN) {
            eeprom.BH_SchmittInput = true;
        } else {
            eeprom.BH_SchmittInput = false;
        }
        short datax0B = (short) ((data[11] & TPRDRV) >> AL_DRIVE_CURRENT);
        if (datax0B < AL_FAST_SLEW) {
            eeprom.TPRDRV = datax0B;
        } else {
            eeprom.TPRDRV = 0;
        }
        int addr = data[7] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
        short s = this.mEepromType;
        if (r0 == (short) 70) {
            eeprom.Manufacturer = getStringDescriptor((addr - 128) / 2, data);
            eeprom.Product = getStringDescriptor(((data[AL_SCHMITT_INPUT] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) - 128) / 2, data);
            eeprom.SerialNumber = getStringDescriptor(((data[9] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) - 128) / 2, data);
            return eeprom;
        }
        eeprom.Manufacturer = getStringDescriptor(addr / 2, data);
        eeprom.Product = getStringDescriptor((data[AL_SCHMITT_INPUT] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2, data);
        eeprom.SerialNumber = getStringDescriptor((data[9] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2, data);
        return eeprom;
    }

    int getUserSize() {
        int data = readWord((short) 9);
        return (((this.mEepromSize - 1) - 1) - ((((data & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / 2) + (((65280 & data) >> AL_SCHMITT_INPUT) / 2)) + 1)) * 2;
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
