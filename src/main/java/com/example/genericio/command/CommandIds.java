package com.example.genericio.command;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommandIds {
    PING_COMMAND,
    PONG_COMMAND,
    WRITE_PIN,
    WRITE_PIN_RESPONSE,
    READ_PIN,
    READ_PIN_RESPONSE,
    GPIO_INIT,
    GPIO_INIT_RESPONSE,

    TIM_START,
    TIM_START_RESPONSE,

    TIM_STOP,
    TIM_STOP_RESPONSE,

    TIM_INIT,
    TIM_INIT_RESPONSE,

    TIM_DEINIT,
    TIM_DEINIT_RESPONSE,

    TIM_CONFIG_CHANNEL,
    TIM_CONFIG_CHANNEL_RESPONSE
}
