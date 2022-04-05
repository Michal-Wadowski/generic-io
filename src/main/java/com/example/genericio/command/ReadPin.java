package com.example.genericio.command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static com.example.genericio.command.GPIO.Pin;
import static com.example.genericio.command.GPIO.Port;

public class ReadPin implements GenericCommand {

    private Port port;

    private Pin pin;

    @Override
    public byte[] getBytes() {
        return ByteBuffer.allocate(7).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) 5)
                .putShort((short) CommandIds.READ_PIN.ordinal())
                .put((byte) (port.value & 0xff))
                .putShort((short) pin.value)
                .array();
    }

    public ReadPin setPort(Port port) {
        this.port = port;
        return this;
    }

    public ReadPin setPin(Pin pin) {
        this.pin = pin;
        return this;
    }
}
