package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TimStop extends GenericCommand {

    @NonNull
    private final TIM.Mode mode;

    @NonNull
    private final TIM.Timer timer;

    @NonNull
    private final TIM.Channel channel;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_STOP.ordinal())
                .addByte(mode.value)
                .addByte(timer.value)
                .addByte(channel.value);

        return wrapContentAndGetBytes();
    }

}
