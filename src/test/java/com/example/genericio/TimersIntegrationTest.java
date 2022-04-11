package com.example.genericio;

import com.example.genericio.command.*;
import com.example.genericio.response.ReadPinResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static com.example.genericio.command.GPIO.Pin.GPIO_PIN_8;
import static com.example.genericio.command.GPIO.Port;
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
                        .prescaler(64 - 1)
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