package com.jparzonka.time_interval_app.data;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Locale;

/**
 * Created by Jakub on 2016-12-29.
 */

public class BitSetHandler {
    private DTO dto;

    public BitSetHandler(DTO dto) {
        this.dto = dto;
        Log.i("BitSetHandler", "Constructor!");
    }

    private BitSet getST(DTO dto) {
        double pulseWidth = dto.getOutputWidth();
        BitSet bs = new BitSet();
        switch ((int) pulseWidth) {
            case 10:
                bs.set(0, false);
                bs.set(1, false);
                return bs;
            case 20:
                bs.set(0, true);
                bs.set(1, false);
                return bs;
            case 50:
                bs.set(0, false);
                bs.set(1, true);
                return bs;
            case 100:
                bs.set(0, true);
                bs.set(1, true);
                return bs;
            default:
                Log.e("BitSetHandler", "Wrong pulse width value!");
                return null;
        }
    }

    public byte[] getSET_S_Address() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.SET_S.getAddress());
        Log.i("BitSetHandler", "getSET_S_Address!");
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getSET_S_DataArray() {
//        BitSet bs = getST(dto);
//        BitSet data = new BitSet();
//        //ST0
//        Log.i("ST0", valueOf(bs.get(0)));
//        data.set(0, bs.get(0));
//        //ST1
//        Log.i("ST1", valueOf(bs.get(1)));
//        data.set(1, bs.get(1));
//        //XX
//        data.set(2, 4, false);
//        //MODE
//        if (dto.getSelectedMode() == "TI") {
//            data.set(4, false);
//            Log.i("MODE", "TI");
//        } else if (dto.getSelectedMode() == "F") {
//            data.set(4, true);
//            Log.i("MODE", "FREQUENCY");
//        } else Log.e("BitSetHandler", "Wrong selected mode");
//        //CK10M_INT
//        if (dto.isExternalClockSelected()) {
//            Log.i("EXTERNAL CLOCK", "YES");
//            data.set(5, false);
//        } else {
//            Log.i("EXTERNAL CLOCK", "NO");
//            data.set(5, true);
//        }
//        //XX
//        data.set(6, 8, false);
//        //A2
//        Log.i("A - inverted", valueOf(dto.isHasSignal_A_InvertedPolarization()));
//        data.set(8, dto.isHasSignal_A_InvertedPolarization());
//        //XX
//        data.set(9, 11, false);
//        //B2
//        Log.i("B - inverted", valueOf(dto.isHasSignal_B_InvertedPolarization()));
//        data.set(11, dto.isHasSignal_B_InvertedPolarization());
//        //XX
//        data.set(12, 14, false);
//        //C2
//        Log.i("CW - inverted", valueOf(dto.isHasSignal_CW_InvertedPolarization()));
//        data.set(14, dto.isHasSignal_CW_InvertedPolarization());
//        //X
//        data.set(15, false);
//        //Dopełniam do 3 bajtów danych
//        data.set(16, 32, true);
//        Log.i("data toString", data.toString());
//        byte[] p = data.toByteArray();
//        System.out.println("  QW_" + p.length);
//        for (byte a : p) {
//            System.out.println("  QW_" + a);
//        }
//        return Convert.toByteArray(data);
        byte[] data = Convert.hexStringToByteArray("00000020");
        Log.i("BitSetHandler", "getSET_S_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public byte[] getSET_TRIG_Address() {
        Log.i("BitSetHandler", "getSET_TRIG_Address!");
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.SET_TRIG.getAddress());
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getSET_TRIG_DataArray() {
     /*   BitSet data = new BitSet();
        String s = Integer.toBinaryString(8);
        // TODO przenieść to do oddzielnej metody
        char[] dataCharArray = s.toCharArray();
        int dataSize = dataCharArray.length;
        //  System.out.println("getTRIG_DIV_BitSet char size: " + dataSize + "\narray:\n");
        for (char c : dataCharArray) {
            System.out.println(c + " ");
        }

        int lackOfSpace = 4 - dataSize;
        //pierwsza pętla zapełniająca danymi
        for (int i = 0; i < dataSize; i++) {
            if (dataCharArray[dataSize - i - 1] == '1') {
                data.set(16 + i, true);
            } else if (dataCharArray[dataSize - i - 1] == '0') {
                data.set(16 + i, false);
            } else {
                Log.i("BitSetHandler", "Wrong value in char array");
            }
        }
        data.set(20, 40, false);
        Log.i("BitSetHandler", "TRIG_DIV: " + data.toString());
*/
        byte[] data = {0x00, 0x00, 0x69, (byte) 0xBE};
        //Convert.hexStringToByteArray("000069BE");
        Log.i("BitSetHandler", "getSET_TRIG_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public byte[] getTRIG_DIV_Address() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.SET_TRIG.getAddress());
        Log.i("BitSetHandler", "getTRIG_DIV_Address!");
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getTRIG_DIV_DataArray() {
      /*  BitSet data = new BitSet();
        String s = Integer.toBinaryString(8);
        // TODO przenieść to do oddzielnej metody
        char[] dataCharArray = s.toCharArray();
        int dataSize = dataCharArray.length;
        //  System.out.println("getTRIG_DIV_BitSet char size: " + dataSize + "\narray:\n");
        for (char c : dataCharArray) {
            System.out.println(c + " ");
        }

        int lackOfSpace = 4 - dataSize;
        //pierwsza pętla zapełniająca danymi
        for (int i = 0; i < dataSize; i++) {
            if (dataCharArray[dataSize - i - 1] == '1') {
                data.set(16 + i, true);
            } else if (dataCharArray[dataSize - i - 1] == '0') {
                data.set(16 + i, false);
            } else {
                Log.i("BitSetHandler", "Wrong value in char array");
            }
        }
        data.set(20, 40, false);
        Log.i("BitSetHandler", "TRIG_DIV: " + data.toString());*/
        byte[] data = Convert.hexStringToByteArray("00000002");
        Log.i("BitSetHandler", "getTRIG_DIV_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;

    }

    public byte[] getSYNTH_N_Address() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.SYNTH_N.getAddress());
        Log.i("BitSetHandler", "getSYNTH_N_Address!");
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getSYNTH_N_DataArray() {
        byte[] data = Convert.hexStringToByteArray("00001000");
        Log.i("BitSetHandler", "getSYNTH_N_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public byte[] getADF4360_LOAD_Address() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.ADF4360_LOAD.getAddress());
        Log.i("BitSetHandler", "getADF4360_LOAD_Address!");
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getADF4360_LOAD_R_COUNTER_LATCH_DataArray() {
        byte[] data = Convert.hexStringToByteArray("00340005");
        Log.i("BitSetHandler", "getADF4360_LOAD_R_COUNTER_LATCH_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public byte[] getADF4360_LOAD_CONTROL_LATCH_DataArray() {
        byte[] data = Convert.hexStringToByteArray("000E35E4");
        Log.i("BitSetHandler", "getADF4360_LOAD_CONTROL_LATCH_DataArray!");
        for (byte b : data) {
            System.out.println(b + " " + 0xE4 + " ");
        }
        return data;
    }

    public byte[] getADF4360_LOAD_N_COUNTER_LATCH_DataArray() {
        byte[] data = Convert.hexStringToByteArray("0000230A");
        Log.i("BitSetHandler", "getADF4360_LOAD_N_COUNTER_LATCH_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public byte[] getRESET_Address() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.RESET.getAddress());
        Log.i("BitSetHandler", "getRESET_Address!");
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getRESET_POINTLESS_DataArray() {
        byte[] data = Convert.hexStringToByteArray("01020304");
        Log.i("BitSetHandler", "getRESET_POINTLESS_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public byte[] getCFR1_Address() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.CFR1.getAddress());
        Log.i("BitSetHandler", "getCFR1_Address!");
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getCFR1_DataArray() {
        byte[] data = Convert.hexStringToByteArray("00340005");
        Log.i("BitSetHandler", "getCFR1_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public byte[] getFTW0_Address() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.FTW0.getAddress());
        Log.i("BitSetHandler", "getFTW0_Address!");
        for (byte b : address) {
            System.out.println(b + " ");
        }
        return address;
    }

    public byte[] getFTW0_DataArray() {
        byte[] data = Convert.hexStringToByteArray("3EBB2B3E");
        Log.i("BitSetHandler", "getFTW0_DataArray!");
        for (byte b : data) {
            System.out.println(b + " ");
        }
        return data;
    }

    public void verifyOCT_and_DAC() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df2 = (DecimalFormat) nf; //new DecimalFormat("###.###");
        double OCT = Double.parseDouble(df2.format(dto.getOCT()));
        int i = (int) OCT;
        Log.i("BitSetHandler", "double OCT = " + OCT);
        Log.i("BitSetHandler", "int OCT = " + i);
        String s = Integer.toBinaryString(i);
        Log.i("BitSetHandler", "binary OCT = " + s);

        double DAC = Double.parseDouble(df2.format(dto.getDAC()));
        i = (int) DAC;
        s = Convert.convert(DAC);
        //s = Long.toBinaryString(Long.valueOf(String.valueOf(i)));
        DAC = Double.valueOf(df2.format(DAC));
        Log.i("BitSetHandler", "double DAC = " + DAC);
        Log.i("BitSetHandler", "int DAC = " + i);
        Log.i("BitSetHandler", "binary DAC = " + s);
    }

}
