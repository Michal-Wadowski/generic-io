package com.example.genericio.command;

public class PingCommand extends GenericCommand {

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.PING_COMMAND.ordinal());

        return wrapContentAndGetBytes();
    }
}
