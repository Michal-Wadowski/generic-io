package com.example.genericio.command;

import com.example.genericio.SerialPortWrapper;
import lombok.AllArgsConstructor;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

@AllArgsConstructor
public class PingCommand implements GenericCommand {

    public static final short COMMAND_ID = 1;

    private final SerialPortWrapper serialPortWrapper;

    @Override
    public void send() {
        serialPortWrapper.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) 2)
                .putShort(COMMAND_ID)
                .array()
        );
    }
}
