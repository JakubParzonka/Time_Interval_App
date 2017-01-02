package com.jparzonka.time_interval_app.data;

import android.util.Log;

import java.util.BitSet;

/**
 * Created by Jakub on 2016-12-29.
 */

public class CommandAddresses {

    public static BitSet getSET_S_BitSetAddres() {
        // 00000000
        BitSet bs = new BitSet();
        bs.set(0, 7, false);
        Log.i("CA", "SET_S - " + bs.toString());
        return bs;
    }

    public static BitSet getRESET_BitSetAddres() {
        // 01000011
        BitSet bs = new BitSet();
        bs.set(0, 1, true);
        bs.set(2, 5, false);
        bs.set(6, true);
        bs.set(7, false);
        Log.i("CA", "RESET - " + bs.toString());
        return bs;
    }


    public static BitSet getSET_TRIG_BitSetAddres() {
        // 01000100
        BitSet bs = new BitSet();
        bs.set(0, 2, false);
        bs.set(3, true);
        bs.set(4, 6, false);
        bs.set(6, true);
        bs.set(7, false);
        Log.i("CA", "SET_TRIG - " + bs.toString());
        return bs;
    }

    public static BitSet getTIRG_DIV_BitSetAddres() {
        // 01000101
        BitSet bs = new BitSet();
        bs.set(0, true);
        bs.set(1, false);
        bs.set(2, true);
        bs.set(3, 5, false);
        bs.set(6, true);
        bs.set(7, false);
        Log.i("CA", "TRIG_DIV - " + bs.toString());
        return bs;
    }

    public static BitSet getSYNTH_N_BitSetAddres() {
        // 0F
        BitSet bs = new BitSet();
        bs.set(0, 1, true);
        bs.set(2, 5, false);
        bs.set(6, true);
        bs.set(7, false);
        Log.i("CA", "SYNTH_N - " + bs.toString());
        return bs;
    }
}
