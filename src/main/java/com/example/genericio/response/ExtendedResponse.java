package com.example.genericio.response;

import com.example.genericio.command.GenericCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.InputStream;
import java.nio.ByteBuffer;

public interface ExtendedResponse {

    void extendedResponse(ExtendedResponseArgument extendedResponse);

    @RequiredArgsConstructor
    @Value
    class ExtendedResponseArgument {
        GenericCommand command;
        InputStream inputStream;
    }

}
