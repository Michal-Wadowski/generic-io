package com.example.genericio.command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PingCommand implements GenericCommand {

    @Override
    public byte[] getBytes() {
        byte[] array = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) 2)
                .putShort((short) CommandIds.PING_COMMAND.ordinal())
                .array();
        return array;
    }
}
