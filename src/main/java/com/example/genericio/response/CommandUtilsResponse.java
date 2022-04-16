package com.example.genericio.response;

import com.example.genericio.command.CommandUtils;

import java.nio.ByteBuffer;
import java.util.stream.IntStream;

public class CommandUtilsResponse implements GenericResponse {

    private byte[] buffer;

    public CommandUtilsResponse(ByteBuffer byteBuffer) {

        byte commandNumber = byteBuffer.get();
        CommandUtils.Command command = CommandUtils.Command.values()[commandNumber];

        if (command == CommandUtils.Command.READ_BUFFER) {
            int adcDataSize = byteBuffer.getInt();
            if (adcDataSize > 0 && adcDataSize <= 20 * 1024) {
                buffer = new byte[adcDataSize];
                byteBuffer.get(buffer);
            }
        }
    }

    public short[] getData12Bit() {
        short[] result = new short[buffer.length / 2];

        IntStream.range(0, buffer.length / 2).forEach(i -> {
            result[i] = (short)((buffer[2*i] & 0xff) + 0x100 * (buffer[2*i+1] & 0xff));
        });
        return result;
    }
}
