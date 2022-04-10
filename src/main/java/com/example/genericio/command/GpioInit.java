package com.example.genericio.command;

import lombok.Builder;

@Builder
public class GpioInit extends GenericCommand {

    private final GPIO.Port port;

    private final GPIO.Pin pin;

    private final GPIO.Mode mode;

    private final GPIO.PullMode pull;

    private final GPIO.Speed speed;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.GPIO_INIT.ordinal())
                .addByte(port.value)
                .addShort(pin.value)
                .addInt(mode.value)
                .addInt(pull != null ? pull.value : 0)
                .addInt(speed.value);

        return wrapContentAndGetBytes();
    }
}
