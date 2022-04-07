package com.example.genericio.command;

import lombok.Builder;

@Builder
public class GpioInit extends GenericCommand {

    private GPIO.Port port;

    private GPIO.Pin pin;

    private GPIO.Mode mode;

    private GPIO.PullMode pull;

    private GPIO.Speed speed;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.GPIO_INIT.ordinal())
                .addByte(port.value)
                .addShort(pin.value)
                .addInt(mode.value)
                .addInt(pull.value)
                .addInt(speed.value);

        return wrapContentAndGetBytes();
    }
}
