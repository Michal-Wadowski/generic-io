package com.example.genericio.command;

import lombok.AllArgsConstructor;
import lombok.Getter;

public abstract class GPIO {
    @AllArgsConstructor
    @Getter
    public enum Port {
        GPIOA(0), GPIOB(1), GPIOC(2), GPIOD(3), GPIOE(4);

        public final int value;
    }

    @AllArgsConstructor
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
}
