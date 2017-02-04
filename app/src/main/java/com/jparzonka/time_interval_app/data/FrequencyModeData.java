package com.jparzonka.time_interval_app.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jakub on 2017-02-02.
 */

public class FrequencyModeData {

    public static List<byte[]> tenMHZ;
    public static List<byte[]> fiveMHZ;
    public static List<byte[]> twoMHZ;
    public static List<byte[]> oneMHZ;

    public static List<byte[]> getTenMHZ() {
        return tenMHZ;
    }

    private static void setTenMHZ(List<byte[]> tenMHZ) {
        FrequencyModeData.tenMHZ = tenMHZ;
    }

    public static List<byte[]> getFiveMHZ() {
        return fiveMHZ;
    }

    private static void setFiveMHZ(List<byte[]> fiveMHZ) {
        FrequencyModeData.fiveMHZ = fiveMHZ;
    }

    public static List<byte[]> getTwoMHZ() {
        return twoMHZ;
    }

    private static void setTwoMHZ(List<byte[]> twoMHZ) {
        FrequencyModeData.twoMHZ = twoMHZ;
    }

    public static List<byte[]> getOneMHZ() {
        return oneMHZ;
    }

    private static void setOneMHZ(List<byte[]> oneMHZ) {
        FrequencyModeData.oneMHZ = oneMHZ;
    }

    public FrequencyModeData() {
        byte[] trig = new byte[]{0x00, 0x00, 0x00, 0x00};
        byte[] s = new byte[]{0x31, 0x22, 0x00, 0x00};
        List<byte[]> tenMHZ = new ArrayList<>(4);
        List<byte[]> fiveMHZ = new ArrayList<>(4);
        List<byte[]> twoMHZ = new ArrayList<>(4);
        List<byte[]> oneMHZ = new ArrayList<>(4);
        tenMHZ.add(0, trig);
        tenMHZ.add(1, new byte[]{0x0C, 0x00, 0x00, 0x00});
        tenMHZ.add(2, new byte[]{(byte) 0xFF, 0x7C, (byte) 0x50, 0x07});
        tenMHZ.add(3, s);

        fiveMHZ.add(0, trig);
        fiveMHZ.add(1, new byte[]{0x1A, 0x00, 0x00, 0x00});
        fiveMHZ.add(2, new byte[]{(byte) 0xFD, (byte) 0x38, (byte) 0xA8, 0x03});
        fiveMHZ.add(3, s);

        twoMHZ.add(0, trig);
        twoMHZ.add(1, new byte[]{0x44, 0x00, 0x00, 0x00});
        twoMHZ.add(2, new byte[]{(byte) 0xFF, (byte) 0x80, 0x76, 0x01});
        twoMHZ.add(3, s);

        oneMHZ.add(0, trig);
        oneMHZ.add(1, new byte[]{(byte) 0x82, 0x00, 0x00, 0x00});
        oneMHZ.add(2, new byte[]{0x08, (byte) 0x42, (byte) 0xBB, 0x00});
        oneMHZ.add(3, s);

        setTenMHZ(tenMHZ);
        setFiveMHZ(fiveMHZ);
        setTwoMHZ(twoMHZ);
        setOneMHZ(oneMHZ);
    }
}
