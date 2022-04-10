package com.example.genericio.command;

import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class TIM {

    @RequiredArgsConstructor
    @Getter
    public enum Mode {
        PWM(0);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Timer {
        TIM1(0),
        Tim2(1),
        Tim3(2),
        Tim4(3);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Channel {
        CHANNEL_1(0x00000000),
        CHANNEL_2(0x00000004),
        CHANNEL_3(0x00000008),
        CHANNEL_4(0x0000000C),
        CHANNEL_ALL(0x0000003C);

        public final int value;
    }


    @RequiredArgsConstructor
    @Getter
    public enum CounterMode {
        UP(0x00000000),
        DOWN(0x00000010),
        CENTERALIGNED1(0x00000020),
        CENTERALIGNED2(0x00000040),
        CENTERALIGNED3(0x00000060);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ClockDivision {
        DIV1(0x00000000),
        DIV2(0x00000100),
        DIV4(0x00000200);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum AutoReload {
        PRELOAD_DISABLE(0x00000000),
        PRELOAD_ENABLE(0x00000080);

        public final int value;
    }


    static final int TIM_CCMR1_OC1M_0 = 0x00000010;
    static final int TIM_CCMR1_OC1M_1 = 0x00000020;
    static final int TIM_CCMR1_OC1M_2 = 0x00000040;

    @RequiredArgsConstructor
    @Getter
    public enum OCMode {
        TIMING(0x00000000),
        ACTIVE(TIM_CCMR1_OC1M_0),
        INACTIVE(TIM_CCMR1_OC1M_1),
        TOGGLE(TIM_CCMR1_OC1M_1 | TIM_CCMR1_OC1M_0),
        PWM1(TIM_CCMR1_OC1M_2 | TIM_CCMR1_OC1M_1),
        PWM2(TIM_CCMR1_OC1M_2 | TIM_CCMR1_OC1M_1 | TIM_CCMR1_OC1M_0),
        FORCED_ACTIVE(TIM_CCMR1_OC1M_2 | TIM_CCMR1_OC1M_0),
        FORCED_INACTIVE(TIM_CCMR1_OC1M_2);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum OCPolarity {
        HIGH(0x00000000),
        LOW(0x00000002);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum OCNPolarity {
        HIGH(0x00000000),
        LOW(0x00000008);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum OCFastMode {
        DISABLE(0x00000000),
        ENABLE(0x00000004);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum OCIdleState {
        SET(0x00000100),
        RESET(0x00000000);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum OCNIdleState {
        SET(0x00000200),
        RESET(0x00000000);

        public final int value;
    }
}
