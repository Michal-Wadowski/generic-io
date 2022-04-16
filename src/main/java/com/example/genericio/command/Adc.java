package com.example.genericio.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class Adc {

    @RequiredArgsConstructor
    @Getter
    public enum Instance {
        ADC1(0),
        ADC2(1);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ScanConvMode {
        SCAN_DISABLE(0x00000000),
        SCAN_ENABLE(0x00000100);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ContinuousConvMode {
        DISABLE(0),
        ENABLE(1);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum DiscontinuousConvMode {
        DISABLE(0),
        ENABLE(1);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum ExternalTrigConv {
        SOFTWARE_START(0x00080000 | 0x00040000 | 0x00020000);
        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum DataAlign {

        DATAALIGN_RIGHT(0x00000000),
        DATAALIGN_LEFT(0x00000800);

        public final int value;
    }


    final static int ADC_SQR3_SQ1_0 = 0x00000001;
    final static int ADC_SQR3_SQ1_1 = 0x00000002;
    final static int ADC_SQR3_SQ1_2 = 0x00000004;
    final static int ADC_SQR3_SQ1_3 = 0x00000008;
    final static int ADC_SQR3_SQ1_4 = 0x00000010;

    @RequiredArgsConstructor
    @Getter
    public enum Channel {
        ADC_CHANNEL_0(0x00000000),
        ADC_CHANNEL_1(ADC_SQR3_SQ1_0),
        ADC_CHANNEL_2(ADC_SQR3_SQ1_1),
        ADC_CHANNEL_3(ADC_SQR3_SQ1_1 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_4(ADC_SQR3_SQ1_2),
        ADC_CHANNEL_5(ADC_SQR3_SQ1_2 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_6(ADC_SQR3_SQ1_2 | ADC_SQR3_SQ1_1),
        ADC_CHANNEL_7(ADC_SQR3_SQ1_2 | ADC_SQR3_SQ1_1 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_8(ADC_SQR3_SQ1_3),
        ADC_CHANNEL_9(ADC_SQR3_SQ1_3 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_10(ADC_SQR3_SQ1_3 | ADC_SQR3_SQ1_1),
        ADC_CHANNEL_11(ADC_SQR3_SQ1_3 | ADC_SQR3_SQ1_1 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_12(ADC_SQR3_SQ1_3 | ADC_SQR3_SQ1_2),
        ADC_CHANNEL_13(ADC_SQR3_SQ1_3 | ADC_SQR3_SQ1_2 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_14(ADC_SQR3_SQ1_3 | ADC_SQR3_SQ1_2 | ADC_SQR3_SQ1_1),
        ADC_CHANNEL_15(ADC_SQR3_SQ1_3 | ADC_SQR3_SQ1_2 | ADC_SQR3_SQ1_1 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_16(ADC_SQR3_SQ1_4),
        ADC_CHANNEL_17(ADC_SQR3_SQ1_4 | ADC_SQR3_SQ1_0),
        ADC_CHANNEL_TEMPSENSOR(ADC_CHANNEL_16.value),
        ADC_CHANNEL_VREFINT(ADC_CHANNEL_17.value);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Rank {
        ADC_REGULAR_RANK_1(0x00000001),
        ADC_REGULAR_RANK_2(0x00000002),
        ADC_REGULAR_RANK_3(0x00000003),
        ADC_REGULAR_RANK_4(0x00000004),
        ADC_REGULAR_RANK_5(0x00000005),
        ADC_REGULAR_RANK_6(0x00000006),
        ADC_REGULAR_RANK_7(0x00000007),
        ADC_REGULAR_RANK_8(0x00000008),
        ADC_REGULAR_RANK_9(0x00000009),
        ADC_REGULAR_RANK_10(0x0000000A),
        ADC_REGULAR_RANK_11(0x0000000B),
        ADC_REGULAR_RANK_12(0x0000000C),
        ADC_REGULAR_RANK_13(0x0000000D),
        ADC_REGULAR_RANK_14(0x0000000E),
        ADC_REGULAR_RANK_15(0x0000000F),
        ADC_REGULAR_RANK_16(0x00000010);

        public final int value;
    }

    private static final int ADC_SMPR2_SMP0_0 = 0x00000001;
    private static final int ADC_SMPR2_SMP0_1 = 0x00000002;
    private static final int ADC_SMPR2_SMP0_2 = 0x00000004;

    @RequiredArgsConstructor
    @Getter
    public enum SamplingTime {
        ADC_SAMPLETIME_1CYCLE_5(0x00000000),
        ADC_SAMPLETIME_7CYCLES_5(ADC_SMPR2_SMP0_0),
        ADC_SAMPLETIME_13CYCLES_5(ADC_SMPR2_SMP0_1),
        ADC_SAMPLETIME_28CYCLES_5(ADC_SMPR2_SMP0_1 | ADC_SMPR2_SMP0_0),
        ADC_SAMPLETIME_41CYCLES_5(ADC_SMPR2_SMP0_2),
        ADC_SAMPLETIME_55CYCLES_5(ADC_SMPR2_SMP0_2 | ADC_SMPR2_SMP0_0),
        ADC_SAMPLETIME_71CYCLES_5(ADC_SMPR2_SMP0_2 | ADC_SMPR2_SMP0_1),
        ADC_SAMPLETIME_239CYCLES_5(ADC_SMPR2_SMP0_2 | ADC_SMPR2_SMP0_1 | ADC_SMPR2_SMP0_0);

        public final int value;
    }
}
