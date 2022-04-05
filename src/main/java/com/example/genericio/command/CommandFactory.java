package com.example.genericio.command;

import com.example.genericio.SerialPortWrapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommandFactory {

    private final SerialPortWrapper serialPortWrapper;

    public PingCommand ping() {
        return new PingCommand(serialPortWrapper);
    }

    public WritePin writePin() {
        return new WritePin(serialPortWrapper);
    }

    public ReadPin readPin() {
        return new ReadPin(serialPortWrapper);
    }

    public GpioInit gpioInit() {
        return new GpioInit(serialPortWrapper);
    }
}
