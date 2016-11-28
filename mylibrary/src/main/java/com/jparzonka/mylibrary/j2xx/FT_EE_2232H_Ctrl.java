/*
package com.jparzonka.mylibrary.j2xx;

import com.jparzonka.mylibrary.j2xx.D2xxManager.D2xxException;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;

class FT_EE_2232H_Ctrl extends FT_EE_Ctrl {
    private static final int AH_DRIVE_CURRENT = 48;
    private static final int AH_FAST_SLEW = 64;
    private static final int AH_SCHMITT_INPUT = 128;
    private static final int AL_DRIVE_CURRENT = 3;
    private static final int AL_FAST_SLEW = 4;
    private static final int AL_SCHMITT_INPUT = 8;
    private static final int A_245_FIFO = 1;
    private static final int A_245_FIFO_TARGET = 2;
    private static final int A_FAST_SERIAL = 4;
    private static final int A_LOAD_VCP_DRIVER = 8;
    private static final int A_UART_RS232 = 0;
    private static final int BH_DRIVE_CURRENT = 12288;
    private static final int BH_FAST_SLEW = 16384;
    private static final int BH_SCHMITT_INPUT = 32768;
    private static final int BL_DRIVE_CURRENT = 768;
    private static final int BL_FAST_SLEW = 1024;
    private static final int BL_SCHMITT_INPUT = 2048;
    private static final String DEFAULT_PID = "6010";
    private static final byte EEPROM_SIZE_LOCATION = (byte) 12;
    private static final int INVERT_CTS = 2048;
    private static final int INVERT_DCD = 16384;
    private static final int INVERT_DSR = 8192;
    private static final int INVERT_DTR = 4096;
    private static final int INVERT_RI = 32768;
    private static final int INVERT_RTS = 1024;
    private static final int INVERT_RXD = 512;
    private static final int INVERT_TXD = 256;
    private static final int TPRDRV = 24;

    FT_EE_2232H_Ctrl(FT_Device usbC) throws D2xxException {
        super(usbC);
        getEepromSize(EEPROM_SIZE_LOCATION);
    }

    short programEeprom(FT_EEPROM ee) {
        int[] dataToWrite = new int[this.mEepromSize];
        if (ee.getClass() != FT_EEPROM_2232H.class) {
            return (short) 1;
        }
        FT_EEPROM_2232H eeprom = (FT_EEPROM_2232H) ee;
        try {
            if (!eeprom.A_UART) {
                if (eeprom.A_FIFO) {
                    dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | A_245_FIFO;
                } else if (eeprom.A_FIFOTarget) {
                    dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | A_245_FIFO_TARGET;
                } else {
                    dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | A_FAST_SERIAL;
                }
            }
            if (eeprom.A_LoadVCP) {
                dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | A_LOAD_VCP_DRIVER;
            }
            if (!eeprom.B_UART) {
                if (eeprom.B_FIFO) {
                    dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | INVERT_TXD;
                } else if (eeprom.B_FIFOTarget) {
                    dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | INVERT_RXD;
                } else {
                    dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | INVERT_RTS;
                }
            }
            if (eeprom.B_LoadVCP) {
                dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | INVERT_CTS;
            }
            if (eeprom.PowerSaveEnable) {
                dataToWrite[A_UART_RS232] = dataToWrite[A_UART_RS232] | INVERT_RI;
            }
            dataToWrite[A_245_FIFO] = eeprom.VendorId;
            dataToWrite[A_245_FIFO_TARGET] = eeprom.ProductId;
            dataToWrite[AL_DRIVE_CURRENT] = 1792;
            dataToWrite[A_FAST_SERIAL] = setUSBConfig(ee);
            dataToWrite[5] = setDeviceControl(ee);
            dataToWrite[6] = A_UART_RS232;
            short driveA = eeprom.AL_DriveCurrent;
            if (driveA == (short) -1) {
                driveA = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | driveA;
            if (eeprom.AL_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | A_FAST_SERIAL;
            }
            if (eeprom.AL_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | A_LOAD_VCP_DRIVER;
            }
            short driveB = eeprom.AH_DriveCurrent;
            if (driveB == (short) -1) {
                driveB = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | ((short) (driveB << A_FAST_SERIAL));
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
            dataToWrite[6] = dataToWrite[6] | ((short) (driveC << A_LOAD_VCP_DRIVER));
            if (eeprom.BL_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | INVERT_RTS;
            }
            if (eeprom.BL_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | INVERT_CTS;
            }
            dataToWrite[6] = dataToWrite[6] | ((short) (eeprom.BH_DriveCurrent << 12));
            if (eeprom.BH_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | INVERT_DCD;
            }
            if (eeprom.BH_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | INVERT_RI;
            }
            boolean eeprom46 = false;
            int offset = 77;
            if (this.mEepromType == (short) 70) {
                offset = 13;
                eeprom46 = true;
            }
            offset = setStringDescriptor(eeprom.Product, dataToWrite, setStringDescriptor(eeprom.Manufacturer, dataToWrite, offset, 7, eeprom46), A_LOAD_VCP_DRIVER, eeprom46);
            if (eeprom.SerNumEnable) {
                offset = setStringDescriptor(eeprom.SerialNumber, dataToWrite, offset, 9, eeprom46);
            }
            switch (eeprom.TPRDRV) {
                case A_UART_RS232 */
/*0*//*
:
                    dataToWrite[11] = A_UART_RS232;
                    break;
                case A_245_FIFO */
/*1*//*
:
                    dataToWrite[11] = A_LOAD_VCP_DRIVER;
                    break;
                case A_245_FIFO_TARGET */
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
                    dataToWrite[11] = A_UART_RS232;
                    break;
            }
            dataToWrite[12] = this.mEepromType;
            if (dataToWrite[A_245_FIFO] == 0 || dataToWrite[A_245_FIFO_TARGET] == 0) {
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
        FT_EEPROM eeprom = new FT_EEPROM_2232H();
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
                i = (short) (i + A_245_FIFO);
            } catch (Exception e) {
                return null;
            }
        }
        int wordx00 = data[A_UART_RS232];
        switch ((short) (wordx00 & 7)) {
            case A_UART_RS232 */
/*0*//*
:
                eeprom.A_UART = true;
                break;
            case A_245_FIFO */
/*1*//*
:
                eeprom.A_FIFO = true;
                break;
            case A_245_FIFO_TARGET */
/*2*//*
:
                eeprom.A_FIFOTarget = true;
                break;
            case A_FAST_SERIAL */
/*4*//*
:
                eeprom.A_FastSerial = true;
                break;
            default:
                eeprom.A_UART = true;
                break;
        }
        if (((short) ((wordx00 & A_LOAD_VCP_DRIVER) >> AL_DRIVE_CURRENT)) == A_245_FIFO) {
            eeprom.A_LoadVCP = true;
            eeprom.A_LoadD2XX = false;
        } else {
            eeprom.A_LoadVCP = false;
            eeprom.A_LoadD2XX = true;
        }
        switch ((short) ((wordx00 & 1792) >> A_LOAD_VCP_DRIVER)) {
            case A_UART_RS232 */
/*0*//*
:
                eeprom.B_UART = true;
                break;
            case A_245_FIFO */
/*1*//*
:
                eeprom.B_FIFO = true;
                break;
            case A_245_FIFO_TARGET */
/*2*//*
:
                eeprom.B_FIFOTarget = true;
                break;
            case A_FAST_SERIAL */
/*4*//*
:
                eeprom.B_FastSerial = true;
                break;
            default:
                eeprom.B_UART = true;
                break;
        }
        if (((short) ((wordx00 & INVERT_CTS) >> 11)) == A_245_FIFO) {
            eeprom.B_LoadVCP = true;
            eeprom.B_LoadD2XX = false;
        } else {
            eeprom.B_LoadVCP = false;
            eeprom.B_LoadD2XX = true;
        }
        if (((short) ((INVERT_RI & wordx00) >> 15)) == A_245_FIFO) {
            eeprom.PowerSaveEnable = true;
        } else {
            eeprom.PowerSaveEnable = false;
        }
        eeprom.VendorId = (short) data[A_245_FIFO];
        eeprom.ProductId = (short) data[A_245_FIFO_TARGET];
        getUSBConfig(eeprom, data[A_FAST_SERIAL]);
        getDeviceControl(eeprom, data[5]);
        switch ((short) (data[6] & AL_DRIVE_CURRENT)) {
            case A_UART_RS232 */
/*0*//*
:
                eeprom.AL_DriveCurrent = (byte) 0;
                break;
            case A_245_FIFO */
/*1*//*
:
                eeprom.AL_DriveCurrent = (byte) 1;
                break;
            case A_245_FIFO_TARGET */
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
        if (((short) (data[6] & A_FAST_SERIAL)) == A_FAST_SERIAL) {
            eeprom.AL_SlowSlew = true;
        } else {
            eeprom.AL_SlowSlew = false;
        }
        short data3x06 = (short) (data[6] & A_LOAD_VCP_DRIVER);
        if (r0 == A_LOAD_VCP_DRIVER) {
            eeprom.AL_SchmittInput = true;
        } else {
            eeprom.AL_SchmittInput = false;
        }
        switch ((short) ((data[6] & AH_DRIVE_CURRENT) >> A_FAST_SERIAL)) {
            case A_UART_RS232 */
/*0*//*
:
                eeprom.AH_DriveCurrent = (byte) 0;
                break;
            case A_245_FIFO */
/*1*//*
:
                eeprom.AH_DriveCurrent = (byte) 1;
                break;
            case A_245_FIFO_TARGET */
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
        if (data7x06 == AH_SCHMITT_INPUT) {
            eeprom.AH_SchmittInput = true;
        } else {
            eeprom.AH_SchmittInput = false;
        }
        switch ((short) ((data[6] & BL_DRIVE_CURRENT) >> A_LOAD_VCP_DRIVER)) {
            case A_UART_RS232 */
/*0*//*
:
                eeprom.BL_DriveCurrent = (byte) 0;
                break;
            case A_245_FIFO */
/*1*//*
:
                eeprom.BL_DriveCurrent = (byte) 1;
                break;
            case A_245_FIFO_TARGET */
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
        if (((short) (data[6] & INVERT_RTS)) == INVERT_RTS) {
            eeprom.BL_SlowSlew = true;
        } else {
            eeprom.BL_SlowSlew = false;
        }
        short data11x06 = (short) (data[6] & INVERT_CTS);
        if (data7x06 == INVERT_CTS) {
            eeprom.BL_SchmittInput = true;
        } else {
            eeprom.BL_SchmittInput = false;
        }
        switch ((short) ((data[6] & BH_DRIVE_CURRENT) >> 12)) {
            case A_UART_RS232 */
/*0*//*
:
                eeprom.BH_DriveCurrent = (byte) 0;
                break;
            case A_245_FIFO */
/*1*//*
:
                eeprom.BH_DriveCurrent = (byte) 1;
                break;
            case A_245_FIFO_TARGET */
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
        if (((short) (data[6] & INVERT_DCD)) == INVERT_DCD) {
            eeprom.BH_SlowSlew = true;
        } else {
            eeprom.BH_SlowSlew = false;
        }
        if (((short) (data[6] & INVERT_RI)) == INVERT_RI) {
            eeprom.BH_SchmittInput = true;
        } else {
            eeprom.BH_SchmittInput = false;
        }
        short datax0B = (short) ((data[11] & TPRDRV) >> AL_DRIVE_CURRENT);
        if (datax0B < A_FAST_SERIAL) {
            eeprom.TPRDRV = datax0B;
        } else {
            eeprom.TPRDRV = A_UART_RS232;
        }
        int addr = data[7] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
        short s = this.mEepromType;
        if (r0 == (short) 70) {
            eeprom.Manufacturer = getStringDescriptor((addr - 128) / A_245_FIFO_TARGET, data);
            eeprom.Product = getStringDescriptor(((data[A_LOAD_VCP_DRIVER] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) - 128) / A_245_FIFO_TARGET, data);
            eeprom.SerialNumber = getStringDescriptor(((data[9] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) - 128) / A_245_FIFO_TARGET, data);
            return eeprom;
        }
        eeprom.Manufacturer = getStringDescriptor(addr / A_245_FIFO_TARGET, data);
        eeprom.Product = getStringDescriptor((data[A_LOAD_VCP_DRIVER] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / A_245_FIFO_TARGET, data);
        eeprom.SerialNumber = getStringDescriptor((data[9] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / A_245_FIFO_TARGET, data);
        return eeprom;
    }

    int getUserSize() {
        int data = readWord((short) 9);
        return (((this.mEepromSize - 1) - 1) - ((((data & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / A_245_FIFO_TARGET) + (((65280 & data) >> A_LOAD_VCP_DRIVER) / A_245_FIFO_TARGET)) + A_245_FIFO)) * A_245_FIFO_TARGET;
    }

    int writeUserData(byte[] data) {
        if (data.length > getUserSize()) {
            return A_UART_RS232;
        }
        int i;
        int[] eeprom = new int[this.mEepromSize];
        for (i = A_UART_RS232; i < this.mEepromSize; i = (short) (i + A_245_FIFO)) {
            eeprom[i] = readWord(i);
        }
        short offset = (short) (((this.mEepromSize - (getUserSize() / A_245_FIFO_TARGET)) - 1) - 1);
        i = A_UART_RS232;
        while (i < data.length) {
            int dataWrite;
            if (i + A_245_FIFO < data.length) {
                dataWrite = data[i + A_245_FIFO] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            } else {
                dataWrite = A_UART_RS232;
            }
            short offset2 = (short) (offset + A_245_FIFO);
            eeprom[offset] = (dataWrite << A_LOAD_VCP_DRIVER) | (data[i] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            i += A_245_FIFO_TARGET;
            offset = offset2;
        }
        if (eeprom[A_245_FIFO] == 0 || eeprom[A_245_FIFO_TARGET] == 0 || !programEeprom(eeprom, this.mEepromSize - 1)) {
            return A_UART_RS232;
        }
        return data.length;
    }

    byte[] readUserData(int length) {
        byte[] data = new byte[length];
        if (length == 0 || length > getUserSize()) {
            return null;
        }
        short offset = (short) (((this.mEepromSize - (getUserSize() / A_245_FIFO_TARGET)) - 1) - 1);
        int i = A_UART_RS232;
        short offset2 = offset;
        while (i < length) {
            offset = (short) (offset2 + A_245_FIFO);
            int dataRead = readWord(offset2);
            if (i + A_245_FIFO < data.length) {
                data[i + A_245_FIFO] = (byte) (dataRead & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            }
            data[i] = (byte) ((65280 & dataRead) >> A_LOAD_VCP_DRIVER);
            i += A_245_FIFO_TARGET;
            offset2 = offset;
        }
        return data;
    }
}
*/
