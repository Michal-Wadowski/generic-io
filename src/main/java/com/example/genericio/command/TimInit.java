package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TimInit extends GenericCommand {

    @NonNull
    private final TIM.Mode mode;

    @NonNull
    private final TIM.Timer timer;

    @NonNull
    private final Integer prescaler;

    @NonNull
    private final TIM.CounterMode counterMode;

    @NonNull
    private final Integer period;

    @NonNull
    private final TIM.ClockDivision clockDivision;

    @NonNull
    private final Integer repetitionCounter;

    @NonNull
    private final TIM.AutoReload autoReloadPreload;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_INIT.ordinal())
                .addByte(mode.value)
                .addByte(timer.value)

                .addInt(prescaler)
                .addInt(counterMode.value)
                .addInt(period)
                .addInt(clockDivision.value)
                .addInt(repetitionCounter)
                .addInt(autoReloadPreload.value);

        return wrapContentAndGetBytes();
    }

}