package com.example.genericio.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class GPIO {
    @RequiredArgsConstructor
    @Getter
    public enum Port {
        GPIOA(0), GPIOB(1), GPIOC(2), GPIOD(3), GPIOE(4);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Pin {
        GPIO_PIN_0(0x0001),
        GPIO_PIN_1(0x0002),
        GPIO_PIN_2(0x0004),
        GPIO_PIN_3(0x0008),
        GPIO_PIN_4(0x0010),
        GPIO_PIN_5(0x0020),
        GPIO_PIN_6(0x0040),
        GPIO_PIN_7(0x0080),
        GPIO_PIN_8(0x0100),
        GPIO_PIN_9(0x0200),
        GPIO_PIN_10(0x0400),
        GPIO_PIN_11(0x0800),
        GPIO_PIN_12(0x1000),
        GPIO_PIN_13(0x2000),
        GPIO_PIN_14(0x4000),
        GPIO_PIN_15(0x8000),
        GPIO_PIN_All(0xFFFF);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Mode {
        INPUT                        (0x00000000),
        OUTPUT_PP                    (0x00000001),
        OUTPUT_OD                    (0x00000011),
        AF_PP                        (0x00000002),
        AF_OD                        (0x00000012),
        AF_INPUT                     (INPUT.ordinal()),

        ANALOG                       (0x00000003),

        IT_RISING                    (0x10110000),
        IT_FALLING                   (0x10210000),
        IT_RISING_FALLING            (0x10310000),

        EVT_RISING                   (0x10120000),
        EVT_FALLING                  (0x10220000),
        EVT_RISING_FALLING           (0x10320000);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum PullMode {
        NOPULL        (0x00000000),
        PULLUP        (0x00000001),
        PULLDOWN      (0x00000002);

        public final int value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum Speed {
        FREQ_LOW              (0x00000002),
        FREQ_MEDIUM           (0x00000001),
        FREQ_HIGH             (0x00000003);

        public final int value;
    }
}
