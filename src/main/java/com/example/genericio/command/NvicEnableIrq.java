package com.example.genericio.command;

import lombok.Builder;

@Builder
public class NvicEnableIrq extends GenericCommand {

    private final IRQ.IRQType irqType;
    private final Boolean enable;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.NVIC_ENABLE_IRQ.ordinal())
                .addInt(irqType.value)
                .addByte(enable ? 1 : 0);

        return wrapContentAndGetBytes();
    }

}
