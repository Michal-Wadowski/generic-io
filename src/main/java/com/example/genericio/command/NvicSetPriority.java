package com.example.genericio.command;

import lombok.Builder;

@Builder
public class NvicSetPriority extends GenericCommand {

    private final IRQ.IRQType irqType;
    private final Integer preemptPriority;
    private final Integer subPriority;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.NVIC_SET_PRIORITY.ordinal())
                .addInt(irqType.value)
                .addInt(preemptPriority)
                .addInt(subPriority);


        return wrapContentAndGetBytes();
    }

}
