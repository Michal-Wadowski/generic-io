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
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "/dev/ttyACM0";
            }
        };

        serialPortWrapper = new SerialPortWrapperImpl(configuration);

        CommandFactory commandFactory = new CommandFactory(serialPortWrapper);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        commandFactory.ping().send();
        GenericResponse response = responseFactory.getResponse();

        // then
        assertThat(response)
                .isNotNull()
                .isInstanceOf(PongResponse.class);
    }

    @Test
    void readPin_should_use_real_communication() throws IOException, InterruptedException {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "/dev/ttyACM0";
            }
        };

        serialPortWrapper = new SerialPortWrapperImpl(configuration);

        CommandFactory commandFactory = new CommandFactory(serialPortWrapper);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        commandFactory.gpioInit()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setMode(Mode.OUTPUT_PP)
                .setPull(PullMode.NOPULL)
                .setSpeed(Speed.FREQ_LOW)
                .send();
        responseFactory.getResponse();

        commandFactory.readPin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .send();
        ReadPinResponse readPinResponseLo = (ReadPinResponse)responseFactory.getResponse();

        // then
        assertThat(readPinResponseLo).isNotNull();
    }

    @Test
    void writePin_and_readPin_should_use_real_communication() throws IOException, InterruptedException {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "/dev/ttyACM0";
            }
        };

        serialPortWrapper = new SerialPortWrapperImpl(configuration);

        CommandFactory commandFactory = new CommandFactory(serialPortWrapper);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        commandFactory.gpioInit()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setMode(Mode.OUTPUT_PP)
                .setPull(PullMode.NOPULL)
                .setSpeed(Speed.FREQ_LOW)
                .send();
        responseFactory.getResponse();

        commandFactory.writePin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setValue(false)
                .send();
        responseFactory.getResponse();

        commandFactory.readPin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .send();
        ReadPinResponse expectedLow = (ReadPinResponse)responseFactory.getResponse();

        commandFactory.writePin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setValue(true)
                .send();
        responseFactory.getResponse();

        commandFactory.readPin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .send();
        ReadPinResponse expectedHigh = (ReadPinResponse)responseFactory.getResponse();

        // then
        assertThat(expectedLow.isSet()).isFalse();
        assertThat(expectedHigh.isSet()).isTrue();
    }



    @Test
    void gpio_init_should_use_real_communication() throws IOException, InterruptedException {
        // given
        Configuration configuration = new Configuration() {
            @Override
            public String getSerialPortPath() {
                return "/dev/ttyACM0";
            }
        };

        serialPortWrapper = new SerialPortWrapperImpl(configuration);

        CommandFactory commandFactory = new CommandFactory(serialPortWrapper);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        commandFactory.writePin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setValue(false)
                .send();
        responseFactory.getResponse();

        commandFactory.gpioInit()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setMode(Mode.OUTPUT_PP)
                .setPull(PullMode.NOPULL)
                .setSpeed(Speed.FREQ_LOW)
                .send();
        responseFactory.getResponse();

        commandFactory.readPin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .send();
        ReadPinResponse expectedLow = (ReadPinResponse)responseFactory.getResponse();

        commandFactory.gpioInit()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setMode(Mode.INPUT)
                .setPull(PullMode.PULLUP)
                .setSpeed(Speed.FREQ_LOW)
                .send();
        responseFactory.getResponse();

        commandFactory.readPin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .send();
        ReadPinResponse expectedHigh = (ReadPinResponse)responseFactory.getResponse();

        commandFactory.gpioInit()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .setMode(Mode.INPUT)
                .setPull(PullMode.PULLDOWN)
                .setSpeed(Speed.FREQ_LOW)
                .send();
        responseFactory.getResponse();

        commandFactory.readPin()
                .setPort(GPIOC)
                .setPin(GPIO_PIN_13)
                .send();
        ReadPinResponse expectedLow2 = (ReadPinResponse)responseFactory.getResponse();

        // then
        assertThat(expectedLow.isSet()).isFalse();
        assertThat(expectedHigh.isSet()).isTrue();
        assertThat(expectedLow2.isSet()).isFalse();
    }
}