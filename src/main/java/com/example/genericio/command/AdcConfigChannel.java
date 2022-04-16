package com.example.genericio.command;

import lombok.Builder;

@Builder
public class AdcConfigChannel extends GenericCommand {

    private final Adc.Instance instance;
    private final Adc.Channel channel;
    private final Adc.Rank rank;
    private final Adc.SamplingTime samplingTime;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.ADC_CONFIG_CHANNEL.ordinal())
                .addByte(instance.value)
                .addInt(channel.value)
                .addInt(rank.value)
                .addInt(samplingTime.value);

        return wrapContentAndGetBytes();
    }

}
