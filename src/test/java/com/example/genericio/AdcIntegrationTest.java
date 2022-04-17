package com.example.genericio;

import com.example.genericio.command.*;
import com.example.genericio.response.CommandUtilsResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.stream.IntStream;

import static com.example.genericio.command.Adc.Channel.ADC_CHANNEL_0;
import static com.example.genericio.command.Adc.Channel.ADC_CHANNEL_1;
import static com.example.genericio.command.Adc.Rank.ADC_REGULAR_RANK_1;
import static com.example.genericio.command.Adc.SamplingTime.ADC_SAMPLETIME_239CYCLES_5;
import static com.example.genericio.command.DMA.Direction.PERIPH_TO_MEMORY;
import static com.example.genericio.command.DMA.MemDataAlignment.MDATAALIGN_HALFWORD;
import static com.example.genericio.command.DMA.MemInc.MINC_ENABLE;
import static com.example.genericio.command.DMA.Mode.CIRCULAR;
import static com.example.genericio.command.DMA.PeriphDataAlignment.PDATAALIGN_HALFWORD;
import static com.example.genericio.command.DMA.PeriphInc.PINC_DISABLE;
import static com.example.genericio.command.DMA.Priority.PRIORITY_LOW;
import static com.example.genericio.command.GPIO.Pin.GPIO_PIN_0;
import static com.example.genericio.command.GPIO.Pin.GPIO_PIN_1;
import static com.example.genericio.command.GPIO.Port.GPIOA;
import static org.assertj.core.api.Assertions.assertThat;

public class AdcIntegrationTest {

    private final Configuration configuration = new Configuration() {
        @Override
        public String getSerialPortPath() {
            return "/dev/ttyACM0";
        }
    };

    private SerialPortWrapper serialPortWrapper;
    private CommandsExecutor commandsExecutor;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        serialPortWrapper = new SerialPortWrapperImpl(configuration);
        commandsExecutor = new CommandsExecutor(serialPortWrapper);
    }

    @AfterEach
    void tearDown() {
        commandsExecutor.sendCommands(
                GpioInit.builder()
                        .port(GPIOA)
                        .pin(GPIO_PIN_0)
                        .mode(GPIO.Mode.INPUT)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build()
        );

        serialPortWrapper.close();
    }

    @Test
    void expect_high_acd_response() throws InterruptedException {
        // given
        adcCommonInit();

        // when
        commandsExecutor.sendCommands(
                WritePin.builder()
                        .port(GPIOA)
                        .pin(GPIO_PIN_0)
                        .value(true)
                        .build()
        );
        Thread.sleep(10);
        double averageHigh = getAveragedAdcResponse();

        // then
        assertThat(averageHigh)
                .isGreaterThan(4000);
    }

    @Test
    void expect_low_acd_response() throws InterruptedException {
        // given
        adcCommonInit();

        // when
        commandsExecutor.sendCommands(
                WritePin.builder()
                        .port(GPIOA)
                        .pin(GPIO_PIN_0)
                        .value(false)
                        .build()
        );
        Thread.sleep(10);
        double averageLow = getAveragedAdcResponse();

        // then
        assertThat(averageLow)
                .isLessThan(100);
    }

    @Test
    void expect_high_after_channel_reconfigure() throws InterruptedException {
        // given
        adcCommonInit();

        commandsExecutor.sendCommands(
                GpioInit.builder()
                        .port(GPIOA)
                        .pin(GPIO_PIN_1)
                        .mode(GPIO.Mode.OUTPUT_PP)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build(),

                WritePin.builder()
                        .port(GPIOA)
                        .pin(GPIO_PIN_0)
                        .value(false)
                        .build(),

                WritePin.builder()
                        .port(GPIOA)
                        .pin(GPIO_PIN_1)
                        .value(true)
                        .build()
        );

        // when
        commandsExecutor.sendCommands(
                AdcConfigChannel.builder()
                        .instance(Adc.Instance.ADC1)
                        .channel(ADC_CHANNEL_1)
                        .rank(ADC_REGULAR_RANK_1)
                        .samplingTime(ADC_SAMPLETIME_239CYCLES_5)
                        .build()
        );


        Thread.sleep(10);
        double averageHigh = getAveragedAdcResponse();

        // then
        assertThat(averageHigh)
                .isGreaterThan(4000);
    }

    private void adcCommonInit() {
        commandsExecutor.sendCommands(
                GpioInit.builder()
                        .port(GPIOA)
                        .pin(GPIO_PIN_0)
                        .mode(GPIO.Mode.OUTPUT_PP)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build(),

                DmaInit.builder()
                        .instance(DMA.Instance.DMA1_Channel1)
                        .direction(PERIPH_TO_MEMORY)
                        .periphInc(PINC_DISABLE)
                        .memInc(MINC_ENABLE)
                        .periphDataAlignment(PDATAALIGN_HALFWORD)
                        .memDataAlignment(MDATAALIGN_HALFWORD)
                        .mode(CIRCULAR)
                        .priority(PRIORITY_LOW)
                        .build(),

                NvicSetPriority.builder()
                        .irqType(IRQ.IRQType.DMA1_Channel1_IRQn)
                        .preemptPriority(0)
                        .subPriority(0)
                        .build(),

                NvicEnableIrq.builder()
                        .irqType(IRQ.IRQType.DMA1_Channel1_IRQn)
                        .enable(true)
                        .build(),

                AdcInit.builder()
                        .instance(Adc.Instance.ADC1)
                        .scanConvMode(Adc.ScanConvMode.SCAN_DISABLE)
                        .continuousConvMode(Adc.ContinuousConvMode.ENABLE)
                        .discontinuousConvMode(Adc.DiscontinuousConvMode.DISABLE)
                        .externalTrigConv(Adc.ExternalTrigConv.SOFTWARE_START)
                        .dataAlign(Adc.DataAlign.DATAALIGN_RIGHT)
                        .nbrOfConversion(1)
                        .build(),

                AdcConfigChannel.builder()
                        .instance(Adc.Instance.ADC1)
                        .channel(ADC_CHANNEL_0)
                        .rank(ADC_REGULAR_RANK_1)
                        .samplingTime(ADC_SAMPLETIME_239CYCLES_5)
                        .build(),

                CommandUtils.copyDataOnConversionComplete(true),

                AdcStart.builder()
                        .instance(Adc.Instance.ADC1)
                        .mode(AdcStart.Mode.ADC_MODE_DMA)
                        .start(true)
                        .size(64)
                        .build()
        );
    }

    private double getAveragedAdcResponse() {
        CommandUtilsResponse adcResponse = (CommandUtilsResponse) commandsExecutor.sendCommand(
                CommandUtils.readBuffer()
        );

        short[] data = adcResponse.getData12Bit();

        double average = IntStream.range(0, data.length)
                .mapToObj(s -> (double) data[s])
                .reduce(0.0, Double::sum)
                / data.length;
        return average;
    }

}
