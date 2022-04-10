package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TimDeInit extends GenericCommand {

    @NonNull
    private final TIM.Mode mode;

    @NonNull
    private final TIM.Timer timer;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_DEINIT.ordinal())
                .addByte(mode.value)
                .addByte(timer.value);

        return wrapContentAndGetBytes();
    }

}