package com.example.genericio;

import java.io.InputStream;

public interface SerialPortWrapper {
    void write(byte[] bytes);

    int bytesAvailable();

    InputStream getInputStream();
}
