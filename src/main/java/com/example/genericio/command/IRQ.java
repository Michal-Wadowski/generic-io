package com.example.genericio.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class IRQ {

    @RequiredArgsConstructor
    @Getter
    public enum IRQType {
        NonMaskableInt_IRQn         (-14),
        HardFault_IRQn              (-13),
        MemoryManagement_IRQn       (-12),
        BusFault_IRQn               (-11),
        UsageFault_IRQn             (-10),
        SVCall_IRQn                 (-5),
        DebugMonitor_IRQn           (-4),
        PendSV_IRQn                 (-2),
        SysTick_IRQn                (-1),
        WWDG_IRQn                   (0),
        PVD_IRQn                    (1),
        TAMPER_IRQn                 (2),
        RTC_IRQn                    (3),
        FLASH_IRQn                  (4),
        RCC_IRQn                    (5),
        EXTI0_IRQn                  (6),
        EXTI1_IRQn                  (7),
        EXTI2_IRQn                  (8),
        EXTI3_IRQn                  (9),
        EXTI4_IRQn                  (10),
        DMA1_Channel1_IRQn          (11),
        DMA1_Channel2_IRQn          (12),
        DMA1_Channel3_IRQn          (13),
        DMA1_Channel4_IRQn          (14),
        DMA1_Channel5_IRQn          (15),
        DMA1_Channel6_IRQn          (16),
        DMA1_Channel7_IRQn          (17),
        ADC1_2_IRQn                 (18),
        USB_HP_CAN1_TX_IRQn         (19),
        USB_LP_CAN1_RX0_IRQn        (20),
        CAN1_RX1_IRQn               (21),
        CAN1_SCE_IRQn               (22),
        EXTI9_5_IRQn                (23),
        TIM1_BRK_IRQn               (24),
        TIM1_UP_IRQn                (25),
        TIM1_TRG_COM_IRQn           (26),
        TIM1_CC_IRQn                (27),
        TIM2_IRQn                   (28),
        TIM3_IRQn                   (29),
        TIM4_IRQn                   (30),
        I2C1_EV_IRQn                (31),
        I2C1_ER_IRQn                (32),
        I2C2_EV_IRQn                (33),
        I2C2_ER_IRQn                (34),
        SPI1_IRQn                   (35),
        SPI2_IRQn                   (36),
        USART1_IRQn                 (37),
        USART2_IRQn                 (38),
        USART3_IRQn                 (39),
        EXTI15_10_IRQn              (40),
        RTC_Alarm_IRQn              (41),
        USBWakeUp_IRQn              (42);
        
        public final int value;
    }
}
