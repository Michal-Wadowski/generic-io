package com.example.genericio;

import lombok.Getter;

@Getter
public class Response {
    private short command;

    public Response(short command) {
        this.command = command;
    }
}
