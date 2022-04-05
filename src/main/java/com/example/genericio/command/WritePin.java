package com.example.genericio.command;

import com.example.genericio.SerialPortWrapper;
import com.example.genericio.command.GPIO.Pin;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.example.genericio.command.GPIO.Port;

public class WritePin implements GenericCommand {

    private final SerialPortWrapper serialPortWrapper;

    private Port port;

    private Pin pin;

    private Boolean value;

    public WritePin(SerialPortWrapper serialPortWrapper) {
        this.serialPortWrapper = serialPortWrapper;
    }

    @Override
    public void send() {
        serialPortWrapper.write(ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) 6)
                .putShort((short) CommandIds.WRITE_PIN.ordinal())
                .put((byte)(port.value & 0xff))
                .putShort((short) pin.value)
                .put((byte)(value ? 1 : 0))
                .array()
        );
    }

    public WritePin setPort(Port port) {
        this.port = port;
        return this;
    }

    public WritePin setPin(Pin pin) {
        this.pin = pin;
        return this;
    }

    public WritePin setValue(boolean value) {
        this.value = value;
        return this;
    }
}
