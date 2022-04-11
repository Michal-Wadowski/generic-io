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

    private final Integer repetitionCounter;

    private final TIM.AutoReload autoReloadPreload;

    private final TIM.EncoderMode encoderMode;

    private final TIM.ICPolarity ic1Polarity;

    private final TIM.ICSelection ic1Selection;

    private final TIM.ICPrescaler ic1Prescaler;

    private final Integer ic1Filter;

    private final TIM.ICPolarity ic2Polarity;

    private final TIM.ICSelection ic2Selection;

    private final TIM.ICPrescaler ic2Prescaler;

    private final Integer ic2Filter;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_INIT.ordinal())
                .addByte(mode.value);

        if (mode == TIM.Mode.PWM) {
            bytesBuilder.addByte(timer.value)
                    .addInt(prescaler)
                    .addInt(counterMode.value)
                    .addInt(period)
                    .addInt(clockDivision.value)
                    .addInt(repetitionCounter)
                    .addInt(autoReloadPreload.value);
        } if (mode == TIM.Mode.ENCODER) {
            bytesBuilder.addByte(timer.value)
                    .addInt(prescaler)
                    .addInt(counterMode.value)
                    .addInt(period)
                    .addInt(clockDivision.value)
                    .addInt(autoReloadPreload.value)
                    .addInt(encoderMode.value)
                    .addInt(ic1Polarity.value)
                    .addInt(ic1Selection.value)
                    .addInt(ic1Prescaler.value)
                    .addInt(ic1Filter)
                    .addInt(ic2Polarity.value)
                    .addInt(ic2Selection.value)
                    .addInt(ic2Prescaler.value)
                    .addInt(ic2Filter);
        }

        return wrapContentAndGetBytes();
    }

}