package com.example.genericio;

import java.io.InputStream;

public interface SerialPortWrapper {
    void write(byte[] bytes);

    InputStream getInputStream();

    void close();
}
