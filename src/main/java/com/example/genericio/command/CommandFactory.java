package com.example.genericio.command;

import com.example.genericio.SerialPortWrapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandFactory {

    private final SerialPortWrapper serialPortWrapper;

    public GenericCommand ping() {
        return new PingCommand(serialPortWrapper);
    }
}
