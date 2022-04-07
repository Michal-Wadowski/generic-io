package com.example.genericio.command;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedList;

public class BytesBuilder {

    private final LinkedList<Object> chunks = new LinkedList<>();

    public BytesBuilder addByte(int value) {
        chunks.add((byte) value);
        return this;
    }

    public BytesBuilder addShort(int value) {
        chunks.add((short) value);
        return this;
    }

    public BytesBuilder addInt(int value) {
        chunks.add(value);
        return this;
    }

    public byte[] getBytes() {
        int size = getSize();
        ByteBuffer byteBuffer = ByteBuffer.allocate(size).order(ByteOrder.LITTLE_ENDIAN);

        for (Object object : chunks) {
            if (object instanceof Byte) {
                byteBuffer.put((byte) object);
            } else if (object instanceof Short) {
                byteBuffer.putShort((short) object);
            } else if (object instanceof Integer) {
                byteBuffer.putInt((int) object);
            }
        }

        return byteBuffer.array();
    }

    public int getSize() {
        return chunks.stream().map(object -> {
            if (object instanceof Byte) {
                return 1;
            } else if (object instanceof Short) {
                return 2;
            } else if (object instanceof Integer) {
                return 4;
            } else
                return 0;
        }).reduce(0, Integer::sum);
    }

    public BytesBuilder addByteFirst(int value) {
        chunks.addFirst((byte) value);
        return this;
    }

    public BytesBuilder addShortFirst(int value) {
        chunks.addFirst((short) value);
        return this;
    }

    public BytesBuilder addIntFirst(int value) {
        chunks.addFirst(value);
        return this;
    }
}
