package com.example.genericio.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommandResponseIds {
    PONG_COMMAND,

    WRITE_PIN_RESPONSE,

    READ_PIN_RESPONSE,

    GPIO_INIT_RESPONSE,

    TIM_START_RESPONSE,

    TIM_STOP_RESPONSE,

    TIM_INIT_RESPONSE,

    TIM_DEINIT_RESPONSE,

    TIM_CONFIG_CHANNEL_RESPONSE,

    TIM_INSTANCE_UPDATE_RESPONSE,

    TIM_INSTANCE_READ_RESPONSE
}
