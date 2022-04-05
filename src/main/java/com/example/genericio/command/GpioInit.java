package com.example.genericio.command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GpioInit implements GenericCommand {

    private GPIO.Port port;

    private GPIO.Pin pin;

    private GPIO.Mode mode;

    private GPIO.PullMode pull;

    private GPIO.Speed speed;

    @Override
    public byte[] getBytes() {
        byte[] array = ByteBuffer.allocate(19).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) 17)
                .putShort((short) CommandIds.GPIO_INIT.ordinal())
                .put((byte) (port.value & 0xff))
                .putShort((short) pin.value)

                .putInt(mode.value)
                .putInt(pull.value)
                .putInt(speed.value)

                .array();
        return array;
    }

    public GpioInit setPort(GPIO.Port port) {
        this.port = port;
        return this;
    }

    public GpioInit setPin(GPIO.Pin pin) {
        this.pin = pin;
        return this;
    }

    public GpioInit setMode(GPIO.Mode value) {
        this.mode = value;
        return this;
    }

    public GpioInit setPull(GPIO.PullMode value) {
        this.pull = value;
        return this;
    }

    public GpioInit setSpeed(GPIO.Speed value) {
        this.speed = value;
        return this;
    }
}
