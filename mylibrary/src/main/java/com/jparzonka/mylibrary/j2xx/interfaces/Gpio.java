package com.jparzonka.mylibrary.j2xx.interfaces;

public interface Gpio {
    int init(int[] iArr);

    int read(int i, boolean[] zArr);

    int write(int i, boolean z);
}
