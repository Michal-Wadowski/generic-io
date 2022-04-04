package com.example.genericio;

import com.fazecast.jSerialComm.SerialPort;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.fazecast.jSerialComm.SerialPort.TIMEOUT_READ_BLOCKING;

public class SerialPortWrapperImpl implements SerialPortWrapper {

    private final SerialPort commPort;

    public SerialPortWrapperImpl(Configuration configuration) throws FileNotFoundException {
        commPort = SerialPort.getCommPort(configuration.getSerialPortPath());
        if (!commPort.openPort()) {
            throw new FileNotFoundException("Can't open " + configuration.getSerialPortPath());
        }
        commPort.setComPortTimeouts(TIMEOUT_READ_BLOCKING, 100, 100);
    }

    @Override
    public void close() {
        commPort.closePort();
    }

    @Override
    public void write(byte[] bytes) {
        commPort.writeBytes(bytes, bytes.length);
    }

    @Override
    public InputStream getInputStream() {
        return commPort.getInputStream();
    }
}
