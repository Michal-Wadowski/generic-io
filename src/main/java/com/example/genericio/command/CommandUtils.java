package com.example.genericio.command;

import lombok.Builder;

@Builder
public class CommandUtils extends GenericCommand {

    private final Command command;

    private boolean enable;

    public static GenericCommand copyDataOnConversionComplete(boolean enable) {
        return CommandUtils.builder()
                .command(Command.COPY_DATA_ON_CONVERSION_COMPLETE)
                .enable(enable)
                .build();
    }

    public static GenericCommand readBuffer() {
        return CommandUtils.builder()
                .command(Command.READ_BUFFER)
                .build();
    }

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.COMMAND_UTILS.ordinal())
                .addByte(command.ordinal());

        if (command == Command.COPY_DATA_ON_CONVERSION_COMPLETE) {
            bytesBuilder.addByte(enable ? 1 : 0);
        }

        return wrapContentAndGetBytes();
    }

    public enum Command {
        COPY_DATA_ON_CONVERSION_COMPLETE,
        READ_BUFFER
    }
}
