package com.example.genericio;

import com.example.genericio.command.CommandFactory;
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

class CommandFactoryIntegrationTest {

    private final CommandFactory commandFactory = new CommandFactory();

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
        GenericResponse response = responseFactory.sendCommand(commandFactory.ping());

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
                commandFactory.gpioInit()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setMode(Mode.OUTPUT_PP)
                        .setPull(PullMode.NOPULL)
                        .setSpeed(Speed.FREQ_LOW),

                commandFactory.writePin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setValue(false),

                commandFactory.readPin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
        ).get(2);

        ReadPinResponse expectedHigh = (ReadPinResponse) responseFactory.sendCommands(
                commandFactory.writePin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setValue(true),

                commandFactory.readPin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
        ).get(1);

        // then
        assertThat(expectedLow.isSet()).isFalse();
        assertThat(expectedHigh.isSet()).isTrue();
    }

    @Test
    void gpio_init_should_use_real_communication() throws IOException, InterruptedException {
        // given
        serialPortWrapper = new SerialPortWrapperImpl(configuration);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        ReadPinResponse expectedLow = (ReadPinResponse) responseFactory.sendCommands(
                commandFactory.writePin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setValue(false),

                commandFactory.gpioInit()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setMode(Mode.OUTPUT_PP)
                        .setPull(PullMode.NOPULL)
                        .setSpeed(Speed.FREQ_LOW),

                commandFactory.readPin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
        ).get(2);

        ReadPinResponse expectedHigh = (ReadPinResponse) responseFactory.sendCommands(
                commandFactory.gpioInit()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setMode(Mode.INPUT)
                        .setPull(PullMode.PULLUP)
                        .setSpeed(Speed.FREQ_LOW),

                commandFactory.readPin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
        ).get(1);

        ReadPinResponse expectedLow2 = (ReadPinResponse) responseFactory.sendCommands(
                commandFactory.gpioInit()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setMode(Mode.INPUT)
                        .setPull(PullMode.PULLDOWN)
                        .setSpeed(Speed.FREQ_LOW),

                commandFactory.readPin()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
        ).get(1);

        // then
        assertThat(expectedLow.isSet()).isFalse();
        assertThat(expectedHigh.isSet()).isTrue();
        assertThat(expectedLow2.isSet()).isFalse();
    }
}