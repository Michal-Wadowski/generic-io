package com.example.genericio.command;

import lombok.Builder;

import static com.example.genericio.command.CommandUtils.Command.READ_BUFFER;

@Builder
public class CommandUtils extends GenericCommand {

    private final Command command;
    private final Integer size;
    private final Integer pointer;

    public static GenericCommand readBuffer() {
        return CommandUtils.builder()
                .command(READ_BUFFER)
                .build();
    }

    @Override
    public byte[] getBytes() {
        switch (command) {
            case READ_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.COMMAND_UTILS.ordinal())
                        .addByte(command.ordinal());
                break;

            case ALLOC_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.COMMAND_UTILS.ordinal())
                        .addByte(command.ordinal())
                        .addShort(size);
                break;

            case FREE_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.COMMAND_UTILS.ordinal())
                        .addByte(command.ordinal())
                        .addInt(pointer);
                break;

            default:
                return null;
        }

        return wrapContentAndGetBytes();
    }

    public enum Command {
        READ_BUFFER,
        ALLOC_BUFFER,
        FREE_BUFFER
    }
}
