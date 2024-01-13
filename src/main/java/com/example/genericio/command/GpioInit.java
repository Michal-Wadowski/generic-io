package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class GpioInit extends GenericCommand {

    @NonNull
    private final GPIO.Port port;

    @NonNull
    private final GPIO.Pin pin;

    @NonNull
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
                .addInt(speed != null ? speed.value : 0);

        return wrapContentAndGetBytes();
    }
}
