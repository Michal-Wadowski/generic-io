package com.example.genericio.command;

import lombok.Builder;

@Builder
public class DmaInit extends GenericCommand {

    private final DMA.Instance instance;
    private final DMA.Direction direction;
    private final DMA.PeriphInc periphInc;
    private final DMA.MemInc memInc;
    private final DMA.PeriphDataAlignment periphDataAlignment;
    private final DMA.MemDataAlignment memDataAlignment;
    private final DMA.Mode mode;
    private final DMA.Priority priority;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.DMA_INIT.ordinal())
                .addByte(instance.value)
                .addInt(direction.value)
                .addInt(periphInc.value)
                .addInt(memInc.value)
                .addInt(periphDataAlignment.value)
                .addInt(memDataAlignment.value)
                .addInt(mode.value)
                .addInt(priority.value);

        return wrapContentAndGetBytes();
    }

}
