package com.example.genericio.response;

import com.example.genericio.command.Buffer;
import lombok.Getter;

import java.io.IOException;
import java.nio.ByteBuffer;

@Getter
public class BufferResponse implements GenericResponse, ExtendedResponse {

    private byte[] data;

    private final Buffer.Command command;

    private int pointer;

    public BufferResponse(ByteBuffer byteBuffer) {
        byte commandNumber = byteBuffer.get();
        command = Buffer.Command.values()[commandNumber];

        if (command == Buffer.Command.ALLOC_BUFFER) {
            pointer = byteBuffer.getInt();
        }
    }

    @Override
    public void extendedResponse(ExtendedResponseArgument extendedResponse) {
        if (command == Buffer.Command.READ_BUFFER) {
            try {
                Buffer bufferCommand = (Buffer) extendedResponse.getCommand();
                data = extendedResponse.getInputStream().readNBytes(bufferCommand.getSize());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
