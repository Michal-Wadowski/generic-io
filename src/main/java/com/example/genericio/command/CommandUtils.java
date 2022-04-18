package com.example.genericio.command;

import lombok.Builder;

@Builder
public class CommandUtils extends GenericCommand {

    private final Command command;

    public static GenericCommand readBuffer() {
        return CommandUtils.builder()
                .command(Command.READ_BUFFER)
                .build();
    }

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.COMMAND_UTILS.ordinal())
                .addByte(command.ordinal());

        return wrapContentAndGetBytes();
    }

    public enum Command {
        READ_BUFFER
    }
}
