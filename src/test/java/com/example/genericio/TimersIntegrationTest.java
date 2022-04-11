package com.example.genericio;

import com.example.genericio.command.*;
import com.example.genericio.response.ReadPinResponse;
import com.example.genericio.response.TimInstanceReadResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static com.example.genericio.command.GPIO.Pin.*;
import static com.example.genericio.command.GPIO.Port;
import static com.example.genericio.command.TIM.EncoderMode.ENCODERMODE_TI1;
import static com.example.genericio.command.TIM.ICPolarity.ICPOLARITY_RISING;
import static com.example.genericio.command.TIM.ICPrescaler.*;
import static com.example.genericio.command.TIM.ICSelection.ICSELECTION_DIRECTTI;
import static com.example.genericio.command.TIM.OCMode.PWM1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimersIntegrationTest {

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
        timPwmStop(commandsExecutor);
        timPwmDeinit(commandsExecutor);

        commandsExecutor.sendCommands(
                TimStop.builder()
                        .mode(TIM.Mode.ENCODER)
                        .timer(TIM.Timer.TIM2)
                        .channel(TIM.Channel.CHANNEL_ALL)
                        .build(),

                TimDeInit.builder()
                        .mode(TIM.Mode.ENCODER)
                        .timer(TIM.Timer.TIM2)
                        .build()
        );

        commandsExecutor.sendCommands(

                GpioInit.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_8)
                        .mode(GPIO.Mode.INPUT)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build()
        );

        serialPortWrapper.close();
    }

    @Test
    void should_run_pwm_timInit_and_timStart() throws InterruptedException {
        // given

        // when
        timPwmInit(commandsExecutor);
        timPwmStart(commandsExecutor);

        // then
        boolean pwmOk = isPwmRunning(commandsExecutor);
        assertThat(pwmOk).isTrue();
    }

    @Test
    void should_stop_pwm_after_timDeInit() throws InterruptedException {
        // given
        timPwmInit(commandsExecutor);
        timPwmStart(commandsExecutor);
        isPwmRunning(commandsExecutor);

        // when
        timPwmDeinit(commandsExecutor);

        // then
        boolean pwmOk = isPwmRunning(commandsExecutor);
        assertThat(pwmOk).isFalse();
    }

    @Test
    void should_stop_pwm_after_timStop() throws InterruptedException {
        // given
        timPwmInit(commandsExecutor);
        timPwmStart(commandsExecutor);

        // when
        isPwmRunning(commandsExecutor);
        timPwmStop(commandsExecutor);

        // then
        boolean pwmOk = isPwmRunning(commandsExecutor);
        assertThat(pwmOk).isFalse();
    }

    @Test
    void instance_update() throws InterruptedException {
        // given
        commandsExecutor.sendCommands(
                TimInit.builder()
                        .mode(TIM.Mode.PWM)
                        .timer(TIM.Timer.TIM1)
                        .prescaler(100*48 - 1)
                        .counterMode(TIM.CounterMode.UP)
                        .period(1000 - 1)
                        .clockDivision(TIM.ClockDivision.DIV1)
                        .repetitionCounter(0)
                        .autoReloadPreload(TIM.AutoReload.PRELOAD_DISABLE)
                        .build(),

                TimConfigChannel.builder()
                        .mode(TIM.Mode.PWM)
                        .timer(TIM.Timer.TIM1)
                        .channel(TIM.Channel.CHANNEL_1)
                        .ocMode(PWM1)
                        .pulse(500)
                        .ocPolarity(TIM.OCPolarity.HIGH)
                        .ocnPolarity(TIM.OCNPolarity.HIGH)
                        .ocFastMode(TIM.OCFastMode.DISABLE)
                        .ocIdleState(TIM.OCIdleState.RESET)
                        .ocnIdleState(TIM.OCNIdleState.RESET)
                        .build(),

                GpioInit.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_8)
                        .mode(GPIO.Mode.AF_PP)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build()
        );
        timPwmStart(commandsExecutor);

        // when
        commandsExecutor.sendCommands(
                TimInstanceUpdate.builder()
                        .timer(TIM.Timer.TIM1)
                        .command(TimInstanceUpdate.Command.SET_AUTORELOAD)
                        .value(0xffff)
                        .build(),

                TimInstanceUpdate.builder()
                        .timer(TIM.Timer.TIM1)
                        .command(TimInstanceUpdate.Command.SET_COMPARE)
                        .channel(TIM.Channel.CHANNEL_1)
                        .value(0xffff / 2)
                        .build(),

                TimInstanceUpdate.builder()
                        .timer(TIM.Timer.TIM1)
                        .command(TimInstanceUpdate.Command.SET_COUNTER)
                        .channel(TIM.Channel.CHANNEL_1)
                        .value(0)
                        .build()
        );

        Thread.sleep(10);

        ReadPinResponse expectedHigh =
                (ReadPinResponse) commandsExecutor.sendCommand(ReadPin.builder().port(Port.GPIOA).pin(GPIO_PIN_8).build());


        // when
        commandsExecutor.sendCommands(
                TimInstanceUpdate.builder()
                        .timer(TIM.Timer.TIM1)
                        .command(TimInstanceUpdate.Command.SET_COUNTER)
                        .channel(TIM.Channel.CHANNEL_1)
                        .value(0xffff / 2 + 1)
                        .build()
        );

        Thread.sleep(10);

        ReadPinResponse expectedLow =
                (ReadPinResponse) commandsExecutor.sendCommand(ReadPin.builder().port(Port.GPIOA).pin(GPIO_PIN_8).build());


        // then
        assertThat(expectedHigh.isSet()).isTrue();
        assertThat(expectedLow.isSet()).isFalse();
    }

    @Test
    void instance_read() throws InterruptedException {
        // given
        timPwmInit(commandsExecutor);
        timPwmStart(commandsExecutor);

        // when
        TimInstanceReadResponse firstRead = (TimInstanceReadResponse) commandsExecutor.sendCommand(
                TimInstanceRead.builder()
                        .timer(TIM.Timer.TIM1)
                        .command(TimInstanceRead.Command.GET_COUNTER)
                        .build()
        );

        TimInstanceReadResponse secondRead = (TimInstanceReadResponse) commandsExecutor.sendCommand(
                TimInstanceRead.builder()
                        .timer(TIM.Timer.TIM1)
                        .command(TimInstanceRead.Command.GET_COUNTER)
                        .build()
        );

        // then
        assertThat(secondRead.getValue()).isGreaterThan(firstRead.getValue());
    }

    @Test
    void encoder_should_count() throws InterruptedException {
        // given
        commandsExecutor.sendCommands(
                TimInit.builder()
                        .mode(TIM.Mode.ENCODER)
                        .timer(TIM.Timer.TIM2)
                        .prescaler(0)
                        .counterMode(TIM.CounterMode.UP)
                        .period(0xffff)
                        .clockDivision(TIM.ClockDivision.DIV1)
                        .autoReloadPreload(TIM.AutoReload.PRELOAD_DISABLE)

                        .encoderMode(ENCODERMODE_TI1)
                        .ic1Polarity(ICPOLARITY_RISING)
                        .ic1Selection(ICSELECTION_DIRECTTI)
                        .ic1Prescaler(ICPSC_DIV1)
                        .ic1Filter(0)
                        .ic2Polarity(ICPOLARITY_RISING)
                        .ic2Selection(ICSELECTION_DIRECTTI)
                        .ic2Prescaler(ICPSC_DIV1)
                        .ic2Filter(0)
                        .build(),

                GpioInit.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_0)
                        .mode(GPIO.Mode.OUTPUT_PP)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build(),

                GpioInit.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_1)
                        .mode(GPIO.Mode.OUTPUT_PP)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build()
        );

        commandsExecutor.sendCommand(
                TimStart.builder()
                        .mode(TIM.Mode.ENCODER)
                        .timer(TIM.Timer.TIM2)
                        .channel(TIM.Channel.CHANNEL_ALL)
                        .build()
        );

        commandsExecutor.sendCommands(
                WritePin.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_0)
                        .value(false)
                        .build(),

                WritePin.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_1)
                        .value(false)
                        .build()
        );

        // when
        TimInstanceReadResponse readFirst = (TimInstanceReadResponse) commandsExecutor.sendCommand(
                TimInstanceRead.builder()
                        .timer(TIM.Timer.TIM2)
                        .command(TimInstanceRead.Command.GET_COUNTER)
                        .build()
        );

        commandsExecutor.sendCommands(
                WritePin.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_0)
                        .value(true)
                        .build(),

                WritePin.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_1)
                        .value(true)
                        .build(),

                WritePin.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_0)
                        .value(false)
                        .build(),

                WritePin.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_1)
                        .value(false)
                        .build()
        );

        TimInstanceReadResponse readSecond = (TimInstanceReadResponse) commandsExecutor.sendCommand(
                TimInstanceRead.builder()
                        .timer(TIM.Timer.TIM2)
                        .command(TimInstanceRead.Command.GET_COUNTER)
                        .build()
        );

        // then
        assertThat(readSecond.getValue()).isGreaterThan(readFirst.getValue());

    }




    private void timPwmStop(CommandsExecutor commandsExecutor) {
        commandsExecutor.sendCommand(
                TimStop.builder().mode(TIM.Mode.PWM).timer(TIM.Timer.TIM1).channel(TIM.Channel.CHANNEL_1).build()
        );
    }

    private void timPwmDeinit(CommandsExecutor commandsExecutor) {
        commandsExecutor.sendCommand(
                TimDeInit.builder().mode(TIM.Mode.PWM).timer(TIM.Timer.TIM1).build()
        );
    }

    private void timPwmStart(CommandsExecutor commandsExecutor) {
        commandsExecutor.sendCommand(
                TimStart.builder().mode(TIM.Mode.PWM).timer(TIM.Timer.TIM1).channel(TIM.Channel.CHANNEL_1).build()
        );
    }

    private void timPwmInit(CommandsExecutor commandsExecutor) {
        commandsExecutor.sendCommands(
                TimInit.builder()
                        .mode(TIM.Mode.PWM)
                        .timer(TIM.Timer.TIM1)
                        .prescaler(10*48 - 1)
                        .counterMode(TIM.CounterMode.UP)
                        .period(1000 - 1)
                        .clockDivision(TIM.ClockDivision.DIV1)
                        .repetitionCounter(0)
                        .autoReloadPreload(TIM.AutoReload.PRELOAD_DISABLE)
                        .build(),

                TimConfigChannel.builder()
                        .mode(TIM.Mode.PWM)
                        .timer(TIM.Timer.TIM1)
                        .channel(TIM.Channel.CHANNEL_1)
                        .ocMode(PWM1)
                        .pulse(500)
                        .ocPolarity(TIM.OCPolarity.HIGH)
                        .ocnPolarity(TIM.OCNPolarity.HIGH)
                        .ocFastMode(TIM.OCFastMode.DISABLE)
                        .ocIdleState(TIM.OCIdleState.RESET)
                        .ocnIdleState(TIM.OCNIdleState.RESET)
                        .build(),

                GpioInit.builder()
                        .port(Port.GPIOA)
                        .pin(GPIO_PIN_8)
                        .mode(GPIO.Mode.AF_PP)
                        .speed(GPIO.Speed.FREQ_LOW)
                        .build()
        );
    }

    private boolean isPwmRunning(CommandsExecutor commandsExecutor) throws InterruptedException {
        boolean first = false;
        boolean pwmOk = false;
        for (int i = 0; i < 50; i++) {
            ReadPinResponse genericResponse =
                    (ReadPinResponse) commandsExecutor.sendCommand(
                            ReadPin.builder().port(Port.GPIOA).pin(GPIO_PIN_8).build()
                    );

            if (i == 0) {
                first = genericResponse.isSet();
            } else {
                if (genericResponse.isSet() != first) {
                    pwmOk = true;
                    break;
                }
            }

            Thread.sleep(1);
        }
        return pwmOk;
    }
}