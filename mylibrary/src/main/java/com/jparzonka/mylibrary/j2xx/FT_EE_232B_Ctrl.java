package com.jparzonka.mylibrary.j2xx;

import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;

class FT_EE_232B_Ctrl extends FT_EE_Ctrl {
    private static final short CHECKSUM_LOCATION = (short) 63;
    private static final short EEPROM_SIZE = (short) 64;
    private static FT_Device ft_device;

    FT_EE_232B_Ctrl(FT_Device usbC) {
        super(usbC);
        ft_device = usbC;
    }

    short programEeprom(FT_EEPROM ee) {
        int[] data = new int[64];
        if (ee.getClass() != FT_EEPROM.class) {
            return (short) 1;
        }
        FT_EEPROM eeprom = ee;
        for (short i = (short) 0; i < EEPROM_SIZE; i = (short) (i + 1)) {
            data[i] = readWord(i);
        }
        try {
            data[1] = eeprom.VendorId;
            data[2] = eeprom.ProductId;
            data[3] = ft_device.mDeviceInfoNode.bcdDevice;
            data[4] = setUSBConfig(ee);
            int saddr = setStringDescriptor(eeprom.Product, data, setStringDescriptor(eeprom.Manufacturer, data, 10, 7, true), 8, true);
            if (eeprom.SerNumEnable) {
                saddr = setStringDescriptor(eeprom.SerialNumber, data, saddr, 9, true);
            }
            if (data[1] == 0 || data[2] == 0) {
                return (short) 2;
            }
            if (programEeprom(data, 63)) {
                return (short) 0;
            }
            return (short) 1;
        } catch (Exception e) {
            e.printStackTrace();
            return (short) 0;
        }
    }

    FT_EEPROM readEeprom() {
        FT_EEPROM eeprom = new FT_EEPROM();
        int[] data = new int[64];
        for (int i = 0; i < 64; i++) {
            data[i] = readWord((short) i);
        }
        try {
            eeprom.VendorId = (short) data[1];
            eeprom.ProductId = (short) data[2];
            getUSBConfig(eeprom, data[4]);
            eeprom.Manufacturer = getStringDescriptor(10, data);
            int addr = 10 + (eeprom.Manufacturer.length() + 1);
            eeprom.Product = getStringDescriptor(addr, data);
            eeprom.SerialNumber = getStringDescriptor(addr + (eeprom.Product.length() + 1), data);
            return eeprom;
        } catch (Exception e) {
            return null;
        }
    }

    int getUserSize() {
        return (((63 - ((((((readWord((short) 7) & 65280) >> 8) / 2) + 10) + (((readWord((short) 8) & 65280) >> 8) / 2)) + 1)) - 1) - (((readWord((short) 9) & 65280) >> 8) / 2)) * 2;
    }

    int writeUserData(byte[] data) {
        if (data.length > getUserSize()) {
            return 0;
        }
        short i;
        int[] eeprom = new int[64];
        for (i = 0; i < 64; i = (short) (i + 1)) {
            eeprom[i] = readWord(i);
        }
        short offset = (short) (65535 & ((short) ((63 - (getUserSize() / 2)) - 1)));
        i = 0;
        while (i < data.length) {
            int dataWrite;
            if (i + 1 < data.length) {
                dataWrite = data[i + 1] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            } else {
                dataWrite = 0;
            }
            short offset2 = (short) (offset + 1);
            eeprom[offset] = (dataWrite << 8) | (data[i] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            i += 2;
            offset = offset2;
        }
        if (eeprom[1] == 0 || eeprom[2] == 0 || !programEeprom(eeprom, 63)) {
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
        short offset = (short) (65535 & ((short) ((63 - (getUserSize() / 2)) - 1)));
        while (i < length) {
            short offset2 = (short) (offset + 1);
            int dataRead = readWord(offset);
            if (i + 1 < data.length) {
                data[i + 1] = (byte) (dataRead & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
            }
            data[i] = (byte) ((65280 & dataRead) >> 8);
            i += 2;
            offset = offset2;
        }
        return data;
    }
}
