package com.jparzonka.mylibrary.j2xx.protocol;

public interface SpiSlaveListener {
    boolean OnDataReceived(SpiSlaveResponseEvent spiSlaveResponseEvent);
}
