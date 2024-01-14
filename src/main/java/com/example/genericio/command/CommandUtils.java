package com.example.genericio.command;

import lombok.Builder;

import static com.example.genericio.command.CommandUtils.Command.READ_ADC_BUFFER;

@Builder
public class CommandUtils extends GenericCommand {

    private final Command command;
    private final Integer size;
    private final Integer pointer;

    public static GenericCommand readBuffer() {
        return CommandUtils.builder()
                .command(READ_ADC_BUFFER)
                .build();
    }

    @Override
    public byte[] getBytes() {
        switch (command) {
            case READ_ADC_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.COMMAND_UTILS.ordinal())
                        .addByte(command.ordinal());
                break;
            default:
                return null;
        }

        return wrapContentAndGetBytes();
    }

    public enum Command {
        READ_ADC_BUFFER
    }
}
