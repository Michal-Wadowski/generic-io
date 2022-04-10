package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TimStart extends GenericCommand {

    @NonNull
    private final TIM.Mode mode;

    @NonNull
    private final TIM.Timer timer;

    @NonNull
    private final TIM.Channel channel;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_START.ordinal())
                .addByte(mode.value)
                .addByte(timer.value)
                .addByte(channel.value);

        return wrapContentAndGetBytes();
    }

}
