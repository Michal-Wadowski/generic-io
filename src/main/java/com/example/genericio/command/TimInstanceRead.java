package com.example.genericio.command;

import lombok.Builder;
import lombok.NonNull;

@Builder
public class TimInstanceRead extends GenericCommand {

    @NonNull
    private final TIM.Timer timer;

    @NonNull
    private final Command command;

    private final TIM.Channel channel;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.TIM_INSTANCE_READ.ordinal())
                .addByte(timer.value)
                .addByte(command.ordinal());

        if (command == Command.GET_COMPARE) {
            bytesBuilder.addByte(channel.value);
        }

        return wrapContentAndGetBytes();
    }

    public enum Command {
        GET_COMPARE,
        GET_AUTORELOAD,
        GET_COUNTER
    }

}
