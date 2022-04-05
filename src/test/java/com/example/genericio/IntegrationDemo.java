package com.example.genericio;

import com.example.genericio.command.CommandFactory;
import com.example.genericio.response.ReadPinResponse;
import com.example.genericio.response.ResponseFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.example.genericio.command.GPIO.*;
import static com.example.genericio.command.GPIO.Pin.GPIO_PIN_12;
import static com.example.genericio.command.GPIO.Pin.GPIO_PIN_13;
import static com.example.genericio.command.GPIO.Port.GPIOB;
import static com.example.genericio.command.GPIO.Port.GPIOC;

@Disabled
class IntegrationDemo {

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
    void show_pin_state() throws IOException, InterruptedException {
        // given
        serialPortWrapper = new SerialPortWrapperImpl(configuration);
        ResponseFactory responseFactory = new ResponseFactory(serialPortWrapper);

        // when
        responseFactory.sendCommands(
                commandFactory.gpioInit()
                        .setPort(GPIOB)
                        .setPin(GPIO_PIN_12)
                        .setMode(Mode.INPUT)
                        .setPull(PullMode.NOPULL)
                        .setSpeed(Speed.FREQ_LOW),

                commandFactory.gpioInit()
                        .setPort(GPIOC)
                        .setPin(GPIO_PIN_13)
                        .setMode(Mode.OUTPUT_PP)
                        .setPull(PullMode.NOPULL)
                        .setSpeed(Speed.FREQ_LOW)
        );

        while (true) {
            ReadPinResponse response = (ReadPinResponse) responseFactory.sendCommand(
                    commandFactory.readPin()
                            .setPort(GPIOB)
                            .setPin(GPIO_PIN_12)
            );

            boolean value = response.isSet();

            responseFactory.sendCommand(
                    commandFactory.writePin().setPort(GPIOC).setPin(GPIO_PIN_13).setValue(value)
            );

            System.out.println("B12: " + value);
            Thread.sleep(10);
        }

    }
}