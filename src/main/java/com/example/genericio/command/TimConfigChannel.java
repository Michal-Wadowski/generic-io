package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TimConfigChannel extends GenericCommand {

    @NonNull
    private final TIM.Mode mode;

    @NonNull
    private final TIM.Timer timer;

    @NonNull
    private final TIM.Channel channel;

    @NonNull
    private final TIM.OCMode ocMode;

    @NonNull
    private final Integer pulse;

    @NonNull
    private final TIM.OCPolarity ocPolarity;
    @NonNull
    private final TIM.OCNPolarity ocnPolarity;
    @NonNull
    private final TIM.OCFastMode ocFastMode;
    @NonNull
    private final TIM.OCIdleState ocIdleState;
    @NonNull
    private final TIM.OCNIdleState ocnIdleState;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_CONFIG_CHANNEL.ordinal())
                .addByte(mode.value)
                .addByte(timer.value)
                .addByte(channel.value)
                .addInt(ocMode.value)
                .addInt(pulse)
                .addInt(ocPolarity.value)
                .addInt(ocnPolarity.value)
                .addInt(ocFastMode.value)
                .addInt(ocIdleState.value)
                .addInt(ocnIdleState.value);

        return wrapContentAndGetBytes();
    }

}