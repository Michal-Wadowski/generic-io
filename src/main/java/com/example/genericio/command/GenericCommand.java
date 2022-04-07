package com.example.genericio.command;

abstract public class GenericCommand {

    protected final BytesBuilder bytesBuilder = new BytesBuilder();

    public abstract byte[] getBytes();

    protected byte[] wrapContentAndGetBytes() {
        int contentSize = bytesBuilder.getSize();
        bytesBuilder.addShortFirst(contentSize);
        return bytesBuilder.getBytes();
    }
}
