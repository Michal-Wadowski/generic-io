package com.example.genericio.command;

import lombok.Builder;

@Builder
public class AdcStart extends GenericCommand {

    private final Adc.Instance instance;
    private final Mode mode;
    private final Boolean start;
    private final Integer size;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.ADC_START.ordinal())
                .addByte(instance.value)
                .addByte(mode.ordinal())
                .addByte(start ? 1 : 0)
                .addInt(size);

        return wrapContentAndGetBytes();
    }

    public enum Mode {
        ADC_MODE_DMA
    }
}
