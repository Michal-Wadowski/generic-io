package com.example.genericio.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class DMA {

    @RequiredArgsConstructor
    @Getter
    public enum Instance {
        DMA1_Channel1(1),
        DMA1_Channel2(2),
        DMA1_Channel3(3),
        DMA1_Channel4(4),
        DMA1_Channel5(5),
        DMA1_Channel6(6),
        DMA1_Channel7(7);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Direction {
        PERIPH_TO_MEMORY(0x00000000),
        MEMORY_TO_PERIPH(0x00000010),
        MEMORY_TO_MEMORY(0x00004000);


        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum PeriphInc {
        PINC_ENABLE(0x00000040),
        PINC_DISABLE(0x00000000);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum MemInc {
        MINC_ENABLE(0x00000080),
        MINC_DISABLE(0x00000000);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum PeriphDataAlignment {
        PDATAALIGN_BYTE(0x00000000),
        PDATAALIGN_HALFWORD(0x00000100),
        PDATAALIGN_WORD(0x00000200);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum MemDataAlignment {
        MDATAALIGN_BYTE(0x00000000),
        MDATAALIGN_HALFWORD(0x00000400),
        MDATAALIGN_WORD(0x00000800);
        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Mode {
        NORMAL(0x00000000),
        CIRCULAR(0x00000020);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Priority {
        PRIORITY_LOW(0x00000000),
        PRIORITY_MEDIUM(0x00001000),
        PRIORITY_HIGH(0x00002000),
        PRIORITY_VERY_HIGH(0x00003000);

        public final int value;
    }

}
