package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TimInstanceUpdate extends GenericCommand {

    @NonNull
    private final Command command;

    @NonNull
    private final TIM.Timer timer;

    private final TIM.Channel channel;

    @NonNull
    private final int value;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_INSTANCE_UPDATE.ordinal())
                .addByte(timer.value)
                .addByte(command.ordinal());

        switch (command) {
            case SET_COMPARE -> bytesBuilder.addByte(channel.value).addInt(value);
            case SET_AUTORELOAD -> bytesBuilder.addInt(value);
            case SET_COUNTER -> bytesBuilder.addInt(value);
        }

        return wrapContentAndGetBytes();
    }

    public enum Command {
        SET_COMPARE,
        SET_AUTORELOAD,
        SET_COUNTER
    }
}
