package com.example.genericio.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommandIds {
    PING_COMMAND,

    WRITE_PIN,

    READ_PIN,

    GPIO_INIT,

    TIM_START,

    TIM_STOP,

    TIM_INIT,

    TIM_DEINIT,

    TIM_CONFIG_CHANNEL,

    TIM_INSTANCE_UPDATE,

    TIM_INSTANCE_READ
}
