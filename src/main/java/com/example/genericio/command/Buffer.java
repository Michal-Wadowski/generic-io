package com.example.genericio.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Buffer extends GenericCommand {

    private final Command command;
    private final Integer size;
    private final Integer pointer;
    private byte[] data;

    @Override
    public byte[] getBytes() {
        switch (command) {
            case ALLOC_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.BUFFER.ordinal())
                        .addByte(command.ordinal())
                        .addShort(size);
                break;

            case FREE_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.BUFFER.ordinal())
                        .addByte(command.ordinal())
                        .addInt(pointer);
                break;

            case READ_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.BUFFER.ordinal())
                        .addByte(command.ordinal())
                        .addInt(pointer)
                        .addShort(size);
                break;

            case WRITE_BUFFER:
                bytesBuilder
                        .addShort(CommandIds.BUFFER.ordinal())
                        .addByte(command.ordinal())
                        .addInt(pointer)
                        .addShort(size);

                for (int i = 0; i < size; i++) {
                    bytesBuilder.addByte(data[i]);
                }

                break;

            default:
                return null;
        }

        return wrapContentAndGetBytes();
    }

    public enum Command {
        ALLOC_BUFFER,
        FREE_BUFFER,
        READ_BUFFER,
        WRITE_BUFFER
    }
}
