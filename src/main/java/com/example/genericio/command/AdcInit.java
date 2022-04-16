package com.example.genericio.command;

import lombok.Builder;

@Builder
public class AdcInit extends GenericCommand {

    private final Adc.Instance instance;
    private final Adc.ScanConvMode scanConvMode;
    private final Adc.ContinuousConvMode continuousConvMode;
    private final Adc.DiscontinuousConvMode discontinuousConvMode;
    private final Adc.ExternalTrigConv externalTrigConv;
    private final Adc.DataAlign dataAlign;
    private final Integer nbrOfConversion;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.ADC_INIT.ordinal())
                .addByte(instance.value)

                .addInt(scanConvMode.value)

                .addByte(continuousConvMode.value)
                .addByte(discontinuousConvMode.value)

                .addInt(externalTrigConv.value)
                .addInt(dataAlign.value)
                .addInt(nbrOfConversion);

        return wrapContentAndGetBytes();
    }

}
