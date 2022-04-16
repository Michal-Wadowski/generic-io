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

    TIM_INSTANCE_READ_RESPONSE,

    DMA_INIT_RESPONSE,

    ADC_INIT_RESPONSE,

    ADC_CONFIG_CHANNEL_RESPONSE,

    ADC_START_RESPONSE,

    NVIC_SET_PRIORITY_RESPONSE,

    NVIC_ENABLE_IRQ_RESPONSE,

    COMMAND_UTILS_RESPONSE
}
