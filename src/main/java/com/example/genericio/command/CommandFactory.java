package com.example.genericio.command;

public class CommandFactory {

    public PingCommand ping() {
        return new PingCommand();
    }

    public WritePin writePin() {
        return new WritePin();
    }

    public ReadPin readPin() {
        return new ReadPin();
    }

    public GpioInit gpioInit() {
        return new GpioInit();
    }
}
