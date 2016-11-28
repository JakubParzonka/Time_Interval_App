package com.jparzonka.mylibrary.j2xx;

import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;

class FT_EE_245R_Ctrl extends FT_EE_Ctrl {
    private static final short EEPROM_SIZE = (short) 80;
    private static final short EE_MAX_SIZE = (short) 1024;
    private static final short ENDOFUSERLOCATION = (short) 63;
    private static final int EXTERNAL_OSCILLATOR = 2;
    private static final int HIGH_CURRENT_IO = 4;
    private static final int INVERT_CTS = 2048;
    private static final int INVERT_DCD = 16384;
    private static final int INVERT_DSR = 8192;
    private static final int INVERT_DTR = 4096;
    private static final int INVERT_RI = 32768;
    private static final int INVERT_RTS = 1024;
    private static final int INVERT_RXD = 512;
    private static final int INVERT_TXD = 256;
    private static final int LOAD_D2XX_DRIVER = 8;
    private static FT_Device ft_device;

    FT_EE_245R_Ctrl(FT_Device usbC) {
        super(usbC);
        ft_device = usbC;
    }

    boolean writeWord(short offset, short value) {
        int wValue = value & 65535;
        int wIndex = offset & 65535;
        boolean rc = false;
        if (offset >= EE_MAX_SIZE) {
            return Boolean.valueOf(String.valueOf(0));
        }
        byte latency = ft_device.getLatencyTimer();
        ft_device.setLatencyTimer((byte) 119);
        if (ft_device.getConnection().controlTransfer(64, 145, wValue, wIndex, null, 0, 0) == 0) {
            rc = true;
        }
        ft_device.setLatencyTimer(latency);
        return rc;
    }

    short programEeprom(FT_EEPROM ee) {
        int[] data = new int[80];
        if (ee.getClass() != FT_EEPROM_245R.class) {
            return (short) 1;
        }
        FT_EEPROM_245R eeprom = (FT_EEPROM_245R) ee;
        for (short i = (short) 0; i < EEPROM_SIZE; i = (short) (i + 1)) {
            data[i] = readWord(i);
        }
        try {
            int wordx00 = 0 | (data[0] & 65280);
            if (eeprom.HighIO) {
                wordx00 |= HIGH_CURRENT_IO;
            }
            if (eeprom.LoadVCP) {
                wordx00 |= LOAD_D2XX_DRIVER;
            }
            if (eeprom.ExternalOscillator) {
                wordx00 |= EXTERNAL_OSCILLATOR;
            } else {
                wordx00 &= 65533;
            }
            data[0] = wordx00;
            data[1] = eeprom.VendorId;
            data[EXTERNAL_OSCILLATOR] = eeprom.ProductId;
            data[3] = 1536;
            data[HIGH_CURRENT_IO] = setUSBConfig(ee);
            int wordx05 = setDeviceControl(ee);
            if (eeprom.InvertTXD) {
                wordx05 |= INVERT_TXD;
            }
            if (eeprom.InvertRXD) {
                wordx05 |= INVERT_RXD;
            }
            if (eeprom.InvertRTS) {
                wordx05 |= INVERT_RTS;
            }
            if (eeprom.InvertCTS) {
                wordx05 |= INVERT_CTS;
            }
            if (eeprom.InvertDTR) {
                wordx05 |= INVERT_DTR;
            }
            if (eeprom.InvertDSR) {
                wordx05 |= INVERT_DSR;
            }
            if (eeprom.InvertDCD) {
                wordx05 |= INVERT_DCD;
            }
            if (eeprom.InvertRI) {
                wordx05 |= INVERT_RI;
            }
            data[5] = wordx05;
            int c2 = eeprom.CBus2 << LOAD_D2XX_DRIVER;
            data[10] = ((eeprom.CBus0 | (eeprom.CBus1 << HIGH_CURRENT_IO)) | c2) | (eeprom.CBus3 << 12);
            data[11] = eeprom.CBus4;
            int saddr = setStringDescriptor(eeprom.Product, data, setStringDescriptor(eeprom.Manufacturer, data, 12, 7, true), LOAD_D2XX_DRIVER, true);
            if (eeprom.SerNumEnable) {
                saddr = setStringDescriptor(eeprom.SerialNumber, data, saddr, 9, true);
            }
            if (data[1] == 0 || data[EXTERNAL_OSCILLATOR] == 0) {
                return (short) 2;
            }
            byte latency = ft_device.getLatencyTimer();
            ft_device.setLatencyTimer((byte) 119);
            boolean returnCode = programEeprom(data, 80);
            ft_device.setLatencyTimer(latency);
            if (returnCode) {
                return (short) 0;
            }
            return (short) 1;
        } catch (Exception e) {
            e.printStackTrace();
            return (short) 0;
        }
    }

    FT_EEPROM readEeprom() {
        FT_EEPROM_245R eeprom = new FT_EEPROM_245R();
        int[] data = new int[80];
        for (int i = 0; i < 80; i++) {
            data[i] = readWord((short) i);
        }
        try {
            if ((data[0] & HIGH_CURRENT_IO) == HIGH_CURRENT_IO) {
                eeprom.HighIO = true;
            } else {
                eeprom.HighIO = false;
            }
            if ((data[0] & LOAD_D2XX_DRIVER) == LOAD_D2XX_DRIVER) {
                eeprom.LoadVCP = true;
            } else {
                eeprom.LoadVCP = false;
            }
            if ((data[0] & EXTERNAL_OSCILLATOR) == EXTERNAL_OSCILLATOR) {
                eeprom.ExternalOscillator = true;
            } else {
                eeprom.ExternalOscillator = false;
            }
            eeprom.VendorId = (short) data[1];
            eeprom.ProductId = (short) data[EXTERNAL_OSCILLATOR];
            getUSBConfig(eeprom, data[HIGH_CURRENT_IO]);
            getDeviceControl(eeprom, data[5]);
            if ((data[5] & INVERT_TXD) == INVERT_TXD) {
                eeprom.InvertTXD = true;
            } else {
                eeprom.InvertTXD = false;
            }
            if ((data[5] & INVERT_RXD) == INVERT_RXD) {
                eeprom.InvertRXD = true;
            } else {
                eeprom.InvertRXD = false;
            }
            if ((data[5] & INVERT_RTS) == INVERT_RTS) {
                eeprom.InvertRTS = true;
            } else {
                eeprom.InvertRTS = false;
            }
            if ((data[5] & INVERT_CTS) == INVERT_CTS) {
                eeprom.InvertCTS = true;
            } else {
                eeprom.InvertCTS = false;
            }
            if ((data[5] & INVERT_DTR) == INVERT_DTR) {
                eeprom.InvertDTR = true;
            } else {
                eeprom.InvertDTR = false;
            }
            if ((data[5] & INVERT_DSR) == INVERT_DSR) {
                eeprom.InvertDSR = true;
            } else {
                eeprom.InvertDSR = false;
            }
            if ((data[5] & INVERT_DCD) == INVERT_DCD) {
                eeprom.InvertDCD = true;
            } else {
                eeprom.InvertDCD = false;
            }
            if ((data[5] & INVERT_RI) == INVERT_RI) {
                eeprom.InvertRI = true;
            } else {
                eeprom.InvertRI = false;
            }
            int temp = data[10];
            eeprom.CBus0 = (byte) (temp & 15);
            eeprom.CBus1 = (byte) ((temp & 240) >> HIGH_CURRENT_IO);
            eeprom.CBus2 = (byte) ((temp & 3840) >> LOAD_D2XX_DRIVER);
            eeprom.CBus3 = (byte) ((temp & 61440) >> 12);
            eeprom.CBus4 = (byte) (data[11] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            eeprom.Manufacturer = getStringDescriptor(((data[7] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) - 128) / EXTERNAL_OSCILLATOR, data);
            eeprom.Product = getStringDescriptor(((data[LOAD_D2XX_DRIVER] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) - 128) / EXTERNAL_OSCILLATOR, data);
            eeprom.SerialNumber = getStringDescriptor(((data[9] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST) - 128) / EXTERNAL_OSCILLATOR, data);
            return eeprom;
        } catch (Exception e) {
            return null;
        }
    }

    int getUserSize() {
        return (((63 - ((((((readWord((short) 7) & 65280) >> LOAD_D2XX_DRIVER) / EXTERNAL_OSCILLATOR) + 12) + (((readWord((short) 8) & 65280) >> LOAD_D2XX_DRIVER) / EXTERNAL_OSCILLATOR)) + 1)) - (((readWord((short) 9) & 65280) >> LOAD_D2XX_DRIVER) / EXTERNAL_OSCILLATOR)) - 1) * EXTERNAL_OSCILLATOR;
    }

    int writeUserData(byte[] data) {
        if (data.length > getUserSize()) {
            return 0;
        }
        short i;
        int[] eeprom = new int[80];
        for (i = 0; i < 80; i = (short) (i + 1)) {
            eeprom[i] = readWord(i);
        }
        short offset = (short) (65535 & ((short) ((63 - (getUserSize() / EXTERNAL_OSCILLATOR)) - 1)));
        i = 0;
        while (i < data.length) {
            int dataWrite;
            if (i + 1 < data.length) {
                dataWrite = data[i + 1] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            } else {
                dataWrite = 0;
            }
            short offset2 = (short) (offset + 1);
            eeprom[offset] = (dataWrite << LOAD_D2XX_DRIVER) | (data[i] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            i += EXTERNAL_OSCILLATOR;
            offset = offset2;
        }
        if (eeprom[1] == 0 || eeprom[EXTERNAL_OSCILLATOR] == 0) {
            return 0;
        }
        byte latency = ft_device.getLatencyTimer();
        ft_device.setLatencyTimer((byte) 119);
        boolean returnCode = programEeprom(eeprom, 63);
        ft_device.setLatencyTimer(latency);
        if (returnCode) {
            return data.length;
        }
        return 0;
    }

    byte[] readUserData(int length) {
        byte[] data = new byte[length];
        if (length == 0 || length > getUserSize()) {
            return null;
        }
        int i = 0;
        short offset = (short) ((63 - (getUserSize() / EXTERNAL_OSCILLATOR)) - 1);
        while (i < length) {
            short offset2 = (short) (offset + 1);
            int dataRead = readWord(offset);
            if (i + 1 < data.length) {
                data[i + 1] = (byte) (dataRead & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            }
            data[i] = (byte) ((65280 & dataRead) >> LOAD_D2XX_DRIVER);
            i += EXTERNAL_OSCILLATOR;
            offset = offset2;
        }
        return data;
    }
}
