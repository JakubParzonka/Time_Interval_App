package com.jparzonka.time_interval_app.data;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.apache.commons.lang3.ArrayUtils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Locale;

import static java.lang.String.valueOf;

/**
 * Created by Jakub on 2016-12-29.
 */

public class CommandsHandler {
    private DataForGenerator dataForGenerator;

    public CommandsHandler(DataForGenerator dataForGenerator) {
        this.dataForGenerator = dataForGenerator;
        Log.i("CommandsHandler", "Constructor!");
    }

    private BitSet getST(DataForGenerator dataForGenerator) {
        double pulseWidth = dataForGenerator.getOutputWidth();
        BitSet bs = new BitSet();
        if (pulseWidth == 10E-9) {
            bs.set(0, false);
            bs.set(1, false);
            return bs;
        } else if (pulseWidth == 20E-9) {
            bs.set(0, true);
            bs.set(1, false);
            return bs;
        } else if (pulseWidth == 50E-9) {
            bs.set(0, false);
            bs.set(1, true);
            return bs;
        } else if (pulseWidth == 100E-9) {
            bs.set(0, true);
            bs.set(1, true);
            return bs;
        } else {
            Log.e("CommandsHandler", "Wrong pulse width value!");
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public byte[] getSET_S() {
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.SET_S.getAddress());
        BitSet data = new BitSet();
        //MODE
        if (dataForGenerator.getSelectedMode() == "TI") {
            BitSet bs = getST(dataForGenerator);
            //ST0
            Log.i("ST0", valueOf(bs.get(0)));
            data.set(0, bs.get(0));
            //ST1
            Log.i("ST1", valueOf(bs.get(1)));
            data.set(1, bs.get(1));
            //XX
            data.set(2, 4, false);
            data.set(4, false);

            Log.i("MODE", "TI");
        } else if (dataForGenerator.getSelectedMode() == "F") {
            // nastawa od promotora
            data.set(0, true);
            data.set(1, 4, false);
            data.set(4, true);
            Log.i("MODE", "FREQUENCY");
        } else Log.e("CommandsHandler", "Wrong selected mode");
        //CK10M_INT
        if (dataForGenerator.isExternalClockSelected()) {
            Log.i("EXTERNAL CLOCK", "YES");
            data.set(5, false);
        } else {
            Log.i("EXTERNAL CLOCK", "NO");
            data.set(5, true);
        }
        //XX
        data.set(6, 8, true);
        //A2
        Log.i("A - inverted", valueOf(dataForGenerator.isHasSignal_A_InvertedPolarization()));
        data.set(8, dataForGenerator.isHasSignal_A_InvertedPolarization());
        //XX
        data.set(9, 11, true);
        //B2
        Log.i("B - inverted", valueOf(dataForGenerator.isHasSignal_B_InvertedPolarization()));
        data.set(11, dataForGenerator.isHasSignal_B_InvertedPolarization());
        //XX
        data.set(12, 14, true);
        //C2
        Log.i("CW - inverted", valueOf(dataForGenerator.isHasSignal_CW_InvertedPolarization()));
        data.set(14, dataForGenerator.isHasSignal_CW_InvertedPolarization());
        //X
        data.set(15, false);
        //Dopełniam do 3 bajtów danych
        data.set(16, 32, true);
        // byte[] rest = new byte[]{0x21, 0x22, 0x00, 0x00};
        Log.i("SET_.toString => ", data.toString());
        byte[] byteArray = ArrayUtils.addAll(address, data.toByteArray());
//        byte[] data = Convert.hexStringToByteArray("20000000");
        Log.i("CommandsHandler", "getSET_S!");
        System.out.println("size = " + byteArray.length);
        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
//        return data;
        return byteArray;

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public byte[] getSET_TRIG() {        Log.i("CommandsHandler", "getSET_TRIG!");
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray(CommandAddresses.SET_TRIG.getAddress()), dataForGenerator.getTriggerFrequency()));

        System.out.println("size = " + byteArray.length);
        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }

    public byte[] getTRIG_DIV() {
        Log.i("CommandsHandler", "getTRIG_DIV!");
        byte[] address = Convert.hexStringToByteArray(CommandAddresses.TIRG_DIV.getAddress());
        System.out.println("Address: " + address[0] + "   ####    Size: " + address.length);
        for (byte b : address) {
            System.out.println(b + " ");
        }
        byte[] data = new byte[4];
        if (dataForGenerator.getSelectedMode().equals(DataForGenerator.TIME_INTERVALS)) {
            data = new byte[]{0x02, 0x00, 0x00, 0x00};
        } else if (dataForGenerator.getSelectedMode().equals(DataForGenerator.FREQUENCY)) {
            data = new byte[]{0x00, 0x00, 0x00, 0x00};
        }
        byte[] byteArray = Convert.addByteArray(address, data);
        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }

    public byte[] getSYNTH_N(byte[] data) {
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray(CommandAddresses.SYNTH_N.getAddress()), data));
        Log.i("CommandsHandler", "getSYNTH_N!");
        System.out.println("size = " + byteArray.length);

        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        verifyOCT_and_DAC();
        return byteArray;
    }


    public byte[] getADF4360_LOAD_R_COUNTER_LATCH() {
        byte[] data = {0x05, 0x00, 0x34, 0x00};
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray((CommandAddresses.ADF4360_LOAD.getAddress())), data));
        Log.i("CommandsHandler", "getADF4360_LOAD_R_COUNTER_LATCH!");
        System.out.println("size = " + byteArray.length);

        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }

    public byte[] getADF4360_LOAD_CONTROL_LATCH() {
        byte[] data = {(byte) 0xE4, 0x35, 0x0E, 0x00};
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray((CommandAddresses.ADF4360_LOAD.getAddress())), data));
        Log.i("CommandsHandler", "getADF4360_LOAD_CONTROL_LATCH!");
        System.out.println("size = " + byteArray.length);

        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }

    public byte[] getADF4360_LOAD_N_COUNTER_LATCH() {
        byte[] data = {0x0A, 0x23, 0x00, 0x00};
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray((CommandAddresses.ADF4360_LOAD.getAddress())), data));
        Log.i("CommandsHandler", "getADF4360_LOAD_N_COUNTER_LATCH!");
        System.out.println("size = " + byteArray.length);

        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }


    public byte[] getRESET() {
        byte[] data = {(byte) 0xE4, 0x35, 0x0E, 0x00};
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray((CommandAddresses.RESET.getAddress())), data));
        Log.i("CommandsHandler", "getRESET!");
        System.out.println("size = " + byteArray.length);

        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }


    public byte[] getCFR1() {
        byte[] data = {(byte) 0x42, 0x00, 0x00, 0x00};
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray((CommandAddresses.CFR1.getAddress())), data));
        Log.i("CommandsHandler", "getCFR1!");
        System.out.println("size = " + byteArray.length);

        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }

    public byte[] getFTW0() {
        byte[] data = {(byte) 0x3E, 0x2B, (byte) 0xEB, 0x3B};
        byte[] byteArray = (ArrayUtils.addAll(Convert.hexStringToByteArray((CommandAddresses.FTW0.getAddress())), data));
        Log.i("CommandsHandler", "getFTW0!");
        System.out.println("size = " + byteArray.length);

        for (byte b : byteArray) {
            System.out.println(b + " ");
        }
        return byteArray;
    }

    public void verifyOCT_and_DAC() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df2 = (DecimalFormat) nf; //new DecimalFormat("###.###");
        double OCT = Double.parseDouble(df2.format(dataForGenerator.getOCT()));
        int i = (int) OCT;
        Log.i("CommandsHandler", "double OCT = " + OCT);
        Log.i("CommandsHandler", "int OCT = " + i);
        String s = Integer.toBinaryString(i);
        Log.i("CommandsHandler", "binary OCT = " + s);

        double DAC = Double.parseDouble(df2.format(dataForGenerator.getDAC()));
        i = (int) DAC;
        s = Convert.convert(DAC);
        DAC = Double.valueOf(df2.format(DAC));
        Log.i("CommandsHandler", "double DAC = " + DAC);
        Log.i("CommandsHandler", "int DAC = " + i);
        Log.i("CommandsHandler", "binary DAC = " + s);
    }

}
