package com.jparzonka.time_interval_app.data;

/**
 * Created by Jakub on 2017-01-30.
 */

public abstract class ByteArrayForSYNTH_N {

    public static byte[] array10ns = {0x01, 0x00, 0x00, 0x00};
    public static byte[] array1micros = {0x52, 0x00, 0x00, 0x00};
    public static byte[] array10micros = {0x34, 0x03, 0x00, 0x00};
    public static byte[] array100micros = {0x00, 0x20, 0x00, 0x00};
    public static byte[] array1ms = {0x00, 0x40, 0x01, 0x00};
    public static byte[] array10ms = {0x00, (byte) 0x80, 0x0C, 0x00};
    public static byte[] array100ms = {0x00, 0x00, 0x7d, 0x00};
    public static byte[] array1s = {0x00, 0x00, (byte) 0xE2, 0x04};
    public static byte[] array0 = {0x00, 0x00, 0x00, 0x00};
}
