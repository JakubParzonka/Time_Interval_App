package com.jparzonka.mylibrary.j2xx;

import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;
import com.jparzonka.mylibrary.j2xx.protocol.SpiSlaveResponseEvent;

class FT_EE_X_Ctrl extends FT_EE_Ctrl {
    private static final int BCD_ENABLE = 1;
    private static final int CBUS_DRIVE = 48;
    private static final int CBUS_SCHMITT = 128;
    private static final int CBUS_SLEW = 64;
    private static final int DBUS_DRIVE = 3;
    private static final int DBUS_SCHMITT = 8;
    private static final int DBUS_SLEW = 4;
    private static final int DEACTIVATE_SLEEP = 4;
    private static final String DEFAULT_PID = "6015";
    private static final int DEVICE_TYPE_EE_LOC = 73;
    private static final short EE_MAX_SIZE = (short) 1024;
    private static final byte FIFO = (byte) 1;
    private static final int FORCE_POWER_ENABLE = 2;
    private static final byte FT1248 = (byte) 2;
    private static final int FT1248_BIT_ORDER = 32;
    private static final int FT1248_CLK_POLARITY = 16;
    private static final int FT1248_FLOW_CTRL = 64;
    private static final byte I2C = (byte) 3;
    private static final int I2C_DISABLE_SCHMITT = 128;
    private static final int INVERT_CTS = 2048;
    private static final int INVERT_DCD = 16384;
    private static final int INVERT_DSR = 8192;
    private static final int INVERT_DTR = 4096;
    private static final int INVERT_RI = 32768;
    private static final int INVERT_RTS = 1024;
    private static final int INVERT_RXD = 512;
    private static final int INVERT_TXD = 256;
    private static final int LOAD_DRIVER = 128;
    private static final int RS485_ECHO = 8;
    private static final byte UART = (byte) 0;
    private static final int VBUS_SUSPEND = 64;
    private static FT_Device ft_device;

    FT_EE_X_Ctrl(FT_Device usbC) {
        super(usbC);
        ft_device = usbC;
        this.mEepromSize = LOAD_DRIVER;
        this.mEepromType = (short) 1;
    }

    short programEeprom(FT_EEPROM ee) {
        int[] dataToWrite = new int[this.mEepromSize];
        short counter = (short) 0;
        if (ee.getClass() != FT_EEPROM_X_Series.class) {
            return (short) 1;
        }
        FT_EEPROM_X_Series eeprom = (FT_EEPROM_X_Series) ee;
        do {
            dataToWrite[counter] = readWord(counter);
            counter = (short) (counter + BCD_ENABLE);
        } while (counter < this.mEepromSize);
        try {
            dataToWrite[0] = 0;
            if (eeprom.BCDEnable) {
                dataToWrite[0] = dataToWrite[0] | BCD_ENABLE;
            }
            if (eeprom.BCDForceCBusPWREN) {
                dataToWrite[0] = dataToWrite[0] | FORCE_POWER_ENABLE;
            }
            if (eeprom.BCDDisableSleep) {
                dataToWrite[0] = dataToWrite[0] | DEACTIVATE_SLEEP;
            }
            if (eeprom.RS485EchoSuppress) {
                dataToWrite[0] = dataToWrite[0] | RS485_ECHO;
            }
            if (eeprom.A_LoadVCP) {
                dataToWrite[0] = dataToWrite[0] | LOAD_DRIVER;
            }
            if (eeprom.PowerSaveEnable) {
                boolean found = false;
                if (eeprom.CBus0 == 17) {
                    found = true;
                }
                if (eeprom.CBus1 == 17) {
                    found = true;
                }
                if (eeprom.CBus2 == 17) {
                    found = true;
                }
                if (eeprom.CBus3 == 17) {
                    found = true;
                }
                if (eeprom.CBus4 == 17) {
                    found = true;
                }
                if (eeprom.CBus5 == 17) {
                    found = true;
                }
                if (eeprom.CBus6 == 17) {
                    found = true;
                }
                if (!found) {
                    return (short) 1;
                }
                dataToWrite[0] = dataToWrite[0] | VBUS_SUSPEND;
            }
            dataToWrite[BCD_ENABLE] = eeprom.VendorId;
            dataToWrite[FORCE_POWER_ENABLE] = eeprom.ProductId;
            dataToWrite[DBUS_DRIVE] = INVERT_DTR;
            dataToWrite[DEACTIVATE_SLEEP] = setUSBConfig(ee);
            dataToWrite[5] = setDeviceControl(ee);
            if (eeprom.FT1248ClockPolarity) {
                dataToWrite[5] = dataToWrite[5] | FT1248_CLK_POLARITY;
            }
            if (eeprom.FT1248LSB) {
                dataToWrite[5] = dataToWrite[5] | FT1248_BIT_ORDER;
            }
            if (eeprom.FT1248FlowControl) {
                dataToWrite[5] = dataToWrite[5] | VBUS_SUSPEND;
            }
            if (eeprom.I2CDisableSchmitt) {
                dataToWrite[5] = dataToWrite[5] | LOAD_DRIVER;
            }
            if (eeprom.InvertTXD) {
                dataToWrite[5] = dataToWrite[5] | INVERT_TXD;
            }
            if (eeprom.InvertRXD) {
                dataToWrite[5] = dataToWrite[5] | INVERT_RXD;
            }
            if (eeprom.InvertRTS) {
                dataToWrite[5] = dataToWrite[5] | INVERT_RTS;
            }
            if (eeprom.InvertCTS) {
                dataToWrite[5] = dataToWrite[5] | INVERT_CTS;
            }
            if (eeprom.InvertDTR) {
                dataToWrite[5] = dataToWrite[5] | INVERT_DTR;
            }
            if (eeprom.InvertDSR) {
                dataToWrite[5] = dataToWrite[5] | INVERT_DSR;
            }
            if (eeprom.InvertDCD) {
                dataToWrite[5] = dataToWrite[5] | INVERT_DCD;
            }
            if (eeprom.InvertRI) {
                dataToWrite[5] = dataToWrite[5] | INVERT_RI;
            }
            dataToWrite[6] = 0;
            short driveA = eeprom.AD_DriveCurrent;
            if (driveA == (short) -1) {
                driveA = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | driveA;
            if (eeprom.AD_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | DEACTIVATE_SLEEP;
            }
            if (eeprom.AD_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | RS485_ECHO;
            }
            short driveC = eeprom.AC_DriveCurrent;
            if (driveC == -1) {
                driveC = (short) 0;
            }
            dataToWrite[6] = dataToWrite[6] | ((short) (driveC << DEACTIVATE_SLEEP));
            if (eeprom.AC_SlowSlew) {
                dataToWrite[6] = dataToWrite[6] | VBUS_SUSPEND;
            }
            if (eeprom.AC_SchmittInput) {
                dataToWrite[6] = dataToWrite[6] | LOAD_DRIVER;
            }
            int offset = setStringDescriptor(eeprom.Product, dataToWrite, setStringDescriptor(eeprom.Manufacturer, dataToWrite, 80, 7, false), RS485_ECHO, false);
            if (eeprom.SerNumEnable) {
                offset = setStringDescriptor(eeprom.SerialNumber, dataToWrite, offset, 9, false);
            }
            dataToWrite[10] = eeprom.I2CSlaveAddress;
            dataToWrite[11] = eeprom.I2CDeviceID & 65535;
            dataToWrite[12] = eeprom.I2CDeviceID >> FT1248_CLK_POLARITY;
            int c0 = eeprom.CBus0;
            if (c0 == -1) {
                c0 = 0;
            }
            int c1 = eeprom.CBus1;
            if (c1 == -1) {
                c1 = 0;
            }
            dataToWrite[13] = (short) (c0 | (c1 << RS485_ECHO));
            int c2 = eeprom.CBus2;
            if (c2 == -1) {
                c2 = 0;
            }
            int c3 = eeprom.CBus3;
            if (c3 == -1) {
                c3 = 0;
            }
            dataToWrite[14] = (short) (c2 | (c3 << RS485_ECHO));
            int c4 = eeprom.CBus4;
            if (c4 == -1) {
                c4 = 0;
            }
            int c5 = eeprom.CBus5;
            if (c5 == -1) {
                c5 = 0;
            }
            dataToWrite[15] = (short) (c4 | (c5 << RS485_ECHO));
            int c6 = eeprom.CBus6;
            if (c6 == -1) {
                c6 = 0;
            }
            dataToWrite[FT1248_CLK_POLARITY] = (short) c6;
            if (dataToWrite[BCD_ENABLE] == 0 || dataToWrite[FORCE_POWER_ENABLE] == 0) {
                return (short) 2;
            }
            if (programXeeprom(dataToWrite, this.mEepromSize - 1)) {
                return (short) 0;
            }
            return (short) 1;
        } catch (Exception e) {
            e.printStackTrace();
            return (short) 0;
        }
    }

    boolean programXeeprom(int[] dataToWrite, int ee_size) {
        int checksumLocation = ee_size;
        int Checksum = 43690;
        int addressCounter = 0;
        do {
            int b;
            int data = dataToWrite[addressCounter] & 65535;
            writeWord((short) addressCounter, (short) data);
            int TempChecksum = (data ^ Checksum) & 65535;
            int a = (TempChecksum << BCD_ENABLE) & 65535;
            if ((INVERT_RI & TempChecksum) > 0) {
                b = BCD_ENABLE;
            } else {
                b = 0;
            }
            Checksum = (a | b) & 65535;
            addressCounter += BCD_ENABLE;
            if (addressCounter == 18) {
                addressCounter = VBUS_SUSPEND;
                continue;
            }
        } while (addressCounter != checksumLocation);
        writeWord((short) checksumLocation, (short) Checksum);
        return true;
    }

    FT_EEPROM readEeprom() {
        FT_EEPROM_X_Series eeprom = new FT_EEPROM_X_Series();
        int[] dataRead = new int[this.mEepromSize];
        short i = (short) 0;
        while (i < this.mEepromSize) {
            try {
                dataRead[i] = readWord(i);
                i = (short) (i + BCD_ENABLE);
            } catch (Exception e) {
                return null;
            }
        }
        if ((dataRead[0] & BCD_ENABLE) > 0) {
            eeprom.BCDEnable = true;
        } else {
            eeprom.BCDEnable = false;
        }
        if ((dataRead[0] & FORCE_POWER_ENABLE) > 0) {
            eeprom.BCDForceCBusPWREN = true;
        } else {
            eeprom.BCDForceCBusPWREN = false;
        }
        if ((dataRead[0] & DEACTIVATE_SLEEP) > 0) {
            eeprom.BCDDisableSleep = true;
        } else {
            eeprom.BCDDisableSleep = false;
        }
        if ((dataRead[0] & RS485_ECHO) > 0) {
            eeprom.RS485EchoSuppress = true;
        } else {
            eeprom.RS485EchoSuppress = false;
        }
        if ((dataRead[0] & VBUS_SUSPEND) > 0) {
            eeprom.PowerSaveEnable = true;
        } else {
            eeprom.PowerSaveEnable = false;
        }
        if ((dataRead[0] & LOAD_DRIVER) > 0) {
            eeprom.A_LoadVCP = true;
            eeprom.A_LoadD2XX = false;
        } else {
            eeprom.A_LoadVCP = false;
            eeprom.A_LoadD2XX = true;
        }
        eeprom.VendorId = (short) dataRead[BCD_ENABLE];
        eeprom.ProductId = (short) dataRead[FORCE_POWER_ENABLE];
        getUSBConfig(eeprom, dataRead[DEACTIVATE_SLEEP]);
        getDeviceControl(eeprom, dataRead[5]);
        if ((dataRead[5] & FT1248_CLK_POLARITY) > 0) {
            eeprom.FT1248ClockPolarity = true;
        } else {
            eeprom.FT1248ClockPolarity = false;
        }
        if ((dataRead[5] & FT1248_BIT_ORDER) > 0) {
            eeprom.FT1248LSB = true;
        } else {
            eeprom.FT1248LSB = false;
        }
        if ((dataRead[5] & VBUS_SUSPEND) > 0) {
            eeprom.FT1248FlowControl = true;
        } else {
            eeprom.FT1248FlowControl = false;
        }
        if ((dataRead[5] & LOAD_DRIVER) > 0) {
            eeprom.I2CDisableSchmitt = true;
        } else {
            eeprom.I2CDisableSchmitt = false;
        }
        if ((dataRead[5] & INVERT_TXD) == INVERT_TXD) {
            eeprom.InvertTXD = true;
        } else {
            eeprom.InvertTXD = false;
        }
        if ((dataRead[5] & INVERT_RXD) == INVERT_RXD) {
            eeprom.InvertRXD = true;
        } else {
            eeprom.InvertRXD = false;
        }
        if ((dataRead[5] & INVERT_RTS) == INVERT_RTS) {
            eeprom.InvertRTS = true;
        } else {
            eeprom.InvertRTS = false;
        }
        if ((dataRead[5] & INVERT_CTS) == INVERT_CTS) {
            eeprom.InvertCTS = true;
        } else {
            eeprom.InvertCTS = false;
        }
        if ((dataRead[5] & INVERT_DTR) == INVERT_DTR) {
            eeprom.InvertDTR = true;
        } else {
            eeprom.InvertDTR = false;
        }
        if ((dataRead[5] & INVERT_DSR) == INVERT_DSR) {
            eeprom.InvertDSR = true;
        } else {
            eeprom.InvertDSR = false;
        }
        if ((dataRead[5] & INVERT_DCD) == INVERT_DCD) {
            eeprom.InvertDCD = true;
        } else {
            eeprom.InvertDCD = false;
        }
        if ((dataRead[5] & INVERT_RI) == INVERT_RI) {
            eeprom.InvertRI = true;
        } else {
            eeprom.InvertRI = false;
        }
        switch ((short) (dataRead[6] & DBUS_DRIVE)) {
            case SpiSlaveResponseEvent.OK /*0*/:
                eeprom.AD_DriveCurrent = UART;
                break;
            case BCD_ENABLE /*1*/:
                eeprom.AD_DriveCurrent = FIFO;
                break;
            case FORCE_POWER_ENABLE /*2*/:
                eeprom.AD_DriveCurrent = FT1248;
                break;
            case DBUS_DRIVE /*3*/:
                eeprom.AD_DriveCurrent = I2C;
                break;
        }
        if (((short) (dataRead[6] & DEACTIVATE_SLEEP)) == (short) 4) {
            eeprom.AD_SlowSlew = true;
        } else {
            eeprom.AD_SlowSlew = false;
        }
        if (((short) (dataRead[6] & RS485_ECHO)) == (short) 8) {
            eeprom.AD_SchmittInput = true;
        } else {
            eeprom.AD_SchmittInput = false;
        }
        switch ((short) ((dataRead[6] & CBUS_DRIVE) >> DEACTIVATE_SLEEP)) {
            case SpiSlaveResponseEvent.OK /*0*/:
                eeprom.AC_DriveCurrent = UART;
                break;
            case BCD_ENABLE /*1*/:
                eeprom.AC_DriveCurrent = FIFO;
                break;
            case FORCE_POWER_ENABLE /*2*/:
                eeprom.AC_DriveCurrent = FT1248;
                break;
            case DBUS_DRIVE /*3*/:
                eeprom.AC_DriveCurrent = I2C;
                break;
        }
        if (((short) (dataRead[6] & VBUS_SUSPEND)) == (short) 64) {
            eeprom.AC_SlowSlew = true;
        } else {
            eeprom.AC_SlowSlew = false;
        }
        if (((short) (dataRead[6] & LOAD_DRIVER)) == (short) 128) {
            eeprom.AC_SchmittInput = true;
        } else {
            eeprom.AC_SchmittInput = false;
        }
        eeprom.I2CSlaveAddress = dataRead[10];
        eeprom.I2CDeviceID = dataRead[11];
        eeprom.I2CDeviceID |= (dataRead[12] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) << FT1248_CLK_POLARITY;
        eeprom.CBus0 = (byte) (dataRead[13] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        eeprom.CBus1 = (byte) ((dataRead[13] >> RS485_ECHO) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        eeprom.CBus2 = (byte) (dataRead[14] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        eeprom.CBus3 = (byte) ((dataRead[14] >> RS485_ECHO) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        eeprom.CBus4 = (byte) (dataRead[15] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        eeprom.CBus5 = (byte) ((dataRead[15] >> RS485_ECHO) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        eeprom.CBus6 = (byte) (dataRead[FT1248_CLK_POLARITY] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        this.mEepromType = (short) (dataRead[DEVICE_TYPE_EE_LOC] >> RS485_ECHO);
        eeprom.Manufacturer = getStringDescriptor((dataRead[7] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / FORCE_POWER_ENABLE, dataRead);
        eeprom.Product = getStringDescriptor((dataRead[RS485_ECHO] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / FORCE_POWER_ENABLE, dataRead);
        eeprom.SerialNumber = getStringDescriptor((dataRead[9] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / FORCE_POWER_ENABLE, dataRead);
        return eeprom;
    }

    int getUserSize() {
        int data = readWord((short) 9);
        return (((this.mEepromSize - 1) - 1) - ((((data & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) / FORCE_POWER_ENABLE) + (((65280 & data) >> RS485_ECHO) / FORCE_POWER_ENABLE)) + BCD_ENABLE)) * FORCE_POWER_ENABLE;
    }

    int writeUserData(byte[] data) {
        if (data.length > getUserSize()) {
            return 0;
        }
        short i;
        int[] eeprom = new int[this.mEepromSize];
        for (i = 0; i < this.mEepromSize; i = (short) (i + BCD_ENABLE)) {
            eeprom[i] = readWord(i);
        }
        short offset = (short) (((this.mEepromSize - (getUserSize() / FORCE_POWER_ENABLE)) - 1) - 1);
        i = 0;
        while (i < data.length) {
            int dataWrite;
            if (i + BCD_ENABLE < data.length) {
                dataWrite = data[i + BCD_ENABLE] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            } else {
                dataWrite = 0;
            }
            short offset2 = (short) (offset + BCD_ENABLE);
            eeprom[offset] = (dataWrite << RS485_ECHO) | (data[i] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            i += FORCE_POWER_ENABLE;
            offset = offset2;
        }
        if (eeprom[BCD_ENABLE] == 0 || eeprom[FORCE_POWER_ENABLE] == 0 || !programXeeprom(eeprom, this.mEepromSize - 1)) {
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
        short offset = (short) (((this.mEepromSize - (getUserSize() / FORCE_POWER_ENABLE)) - 1) - 1);
        while (i < length) {
            short offset2 = (short) (offset + BCD_ENABLE);
            int dataRead = readWord(offset);
            if (i + BCD_ENABLE < data.length) {
                data[i + BCD_ENABLE] = (byte) (dataRead & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            }
            data[i] = (byte) ((65280 & dataRead) >> RS485_ECHO);
            i += FORCE_POWER_ENABLE;
            offset = offset2;
        }
        return data;
    }
}
