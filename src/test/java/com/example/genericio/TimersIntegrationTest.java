package com.example.genericio;

import com.example.genericio.command.*;
import com.example.genericio.response.ReadPinResponse;
import com.example.genericio.response.ResponseFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static com.example.genericio.command.GPIO.Pin.GPIO_PIN_8;
import static com.example.genericio.command.GPIO.Port;
import static com.example.genericio.command.TIM.OCMode.PWM1;
import static com.example.genericio.command.TIM.OCPolarity.HIGH;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class TimersIntegrationTest {

    private final Configuration configuration = new Configuration() {
        @Override
        public String getSerialPortPath() {
            return "/dev/ttyACM0";
        }
    };
    private SerialPortWrapper serialPortWrapper;
    private ResponseFactory responseFactory;

    @BeforeEach
    void setUp() throws FileNotFoundException {
        serialPortWrapper = new SerialPortWrapperImpl(configuration);
        responseFactory = new ResponseFactory(serialPortWrapper);
    }

    @AfterEach
    void tearDown() {
        timPwmStop(responseFactory);
        timPwmDeinit(responseFactory);

        responseFactory.sendCommands(

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
        timPwmInit(responseFactory);
        timPwmStart(responseFactory);

        // then
        boolean pwmOk = isPwmRunning(responseFactory);
        assertThat(pwmOk).isTrue();
    }

    @Test
    void should_stop_pwm_after_timDeInit() throws InterruptedException {
        // given
        timPwmInit(responseFactory);
        timPwmStart(responseFactory);
        isPwmRunning(responseFactory);

        // when
        timPwmDeinit(responseFactory);

        // then
        boolean pwmOk = isPwmRunning(responseFactory);
        assertThat(pwmOk).isFalse();
    }

    @Test
    void should_stop_pwm_after_timStop() throws InterruptedException {
        // given
        timPwmInit(responseFactory);
        timPwmStart(responseFactory);

        // when
        isPwmRunning(responseFactory);
        timPwmStop(responseFactory);

        // then
        boolean pwmOk = isPwmRunning(responseFactory);
        assertThat(pwmOk).isFalse();
    }

    private void timPwmStop(ResponseFactory responseFactory) {
        responseFactory.sendCommand(
                TimStop.builder().mode(TIM.Mode.PWM).timer(TIM.Timer.TIM1).channel(TIM.Channel.CHANNEL_1).build()
        );
    }

    private void timPwmDeinit(ResponseFactory responseFactory) {
        responseFactory.sendCommand(
                TimDeInit.builder().mode(TIM.Mode.PWM).timer(TIM.Timer.TIM1).build()
        );
    }

    private void timPwmStart(ResponseFactory responseFactory) {
        responseFactory.sendCommand(
                TimStart.builder().mode(TIM.Mode.PWM).timer(TIM.Timer.TIM1).channel(TIM.Channel.CHANNEL_1).build()
        );
    }

    private void timPwmInit(ResponseFactory responseFactory) {
        responseFactory.sendCommands(


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

    private boolean isPwmRunning(ResponseFactory responseFactory) throws InterruptedException {
        boolean first = false;
        boolean pwmOk = false;
        for (int i = 0; i < 50; i++) {
            ReadPinResponse genericResponse =
                    (ReadPinResponse) responseFactory.sendCommand(
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