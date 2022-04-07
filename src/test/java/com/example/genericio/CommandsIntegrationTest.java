package com.example.genericio;

import com.example.genericio.command.GpioInit;
import com.example.genericio.command.PingCommand;
import com.example.genericio.command.ReadPin;
import com.example.genericio.command.WritePin;
import com.example.genericio.response.GenericResponse;
import com.example.genericio.response.PongResponse;
import com.example.genericio.response.ReadPinResponse;
import com.example.genericio.response.ResponseFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static com.example.genericio.command.GPIO.*;
import static com.example.genericio.command.GPIO.Pin.GPIO_PIN_13;
import static com.example.genericio.command.GPIO.Port.GPIOC;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandsIntegrationTest {

    private final Configuration configuration = new Configuration() {
        @Override
        public String getSerialPortPath() {
            return "/dev/ttyACM0";
        }
    };
    private SerialPortWrapper serialPortWrapper;

    @BeforeEach
    void setUp() {
        serialPortWrapper = null;
    }

    @AfterEach
    void tearDown() {
        if (serialPortWrapper != null) {
            serialPortWrapper.close();
        }
    }

    @Test
    void new_SerialPort_should_fails_if_no_serial_device() {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "ttyFake";
            }
        };

        // when/then
        assertThrows(FileNotFoundException.class, () -> new SerialPortWrapperImpl(configuration));
    }

    @Test
    void ping_and_getResponse_should_use_real_communication() throws IOException {
        // given
        serialPortWrapper = new SerialPortWrapperImpl(configuration);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        GenericResponse response = responseFactory.sendCommand(new PingCommand());

        // then
        assertThat(response)
                .isNotNull()
                .isInstanceOf(PongResponse.class);
    }

    @Test
    void writePin_and_readPin_should_use_real_communication() throws IOException {
        // given
        serialPortWrapper = new SerialPortWrapperImpl(configuration);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        ReadPinResponse expectedLow = (ReadPinResponse) responseFactory.sendCommands(
                GpioInit.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .mode(Mode.OUTPUT_PP)
                        .pull(PullMode.NOPULL)
                        .speed(Speed.FREQ_LOW)
                        .build(),

                WritePin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .value(false)
                        .build(),

                ReadPin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .build()
        ).get(2);

        ReadPinResponse expectedHigh = (ReadPinResponse) responseFactory.sendCommands(
                WritePin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .value(true)
                        .build(),

                ReadPin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .build()
        ).get(1);

        // then
        assertThat(expectedLow.isSet()).isFalse();
        assertThat(expectedHigh.isSet()).isTrue();
    }

    @Test
    void gpio_init_should_use_real_communication() throws IOException {
        // given
        serialPortWrapper = new SerialPortWrapperImpl(configuration);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        ReadPinResponse expectedLow = (ReadPinResponse) responseFactory.sendCommands(
                WritePin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .value(false)
                        .build(),

                GpioInit.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .mode(Mode.OUTPUT_PP)
                        .pull(PullMode.NOPULL)
                        .speed(Speed.FREQ_LOW)
                        .build(),

                ReadPin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .build()
        ).get(2);

        ReadPinResponse expectedHigh = (ReadPinResponse) responseFactory.sendCommands(
                GpioInit.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .mode(Mode.INPUT)
                        .pull(PullMode.PULLUP)
                        .speed(Speed.FREQ_LOW)
                        .build(),

                ReadPin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .build()
        ).get(1);

        ReadPinResponse expectedLow2 = (ReadPinResponse) responseFactory.sendCommands(
                GpioInit.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .mode(Mode.INPUT)
                        .pull(PullMode.PULLDOWN)
                        .speed(Speed.FREQ_LOW)
                        .build(),

                ReadPin.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .build()
        ).get(1);

        // then
        assertThat(expectedLow.isSet()).isFalse();
        assertThat(expectedHigh.isSet()).isTrue();
        assertThat(expectedLow2.isSet()).isFalse();
    }
}