package com.example.genericio;

import com.example.genericio.command.GpioInit;
import com.example.genericio.command.ReadPin;
import com.example.genericio.command.WritePin;
import com.example.genericio.response.ReadPinResponse;
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
        CommandsExecutor commandsExecutor = new CommandsExecutor(serialPortWrapper);

        // when
        commandsExecutor.sendCommands(
                GpioInit.builder()
                        .port(GPIOB)
                        .pin(GPIO_PIN_12)
                        .mode(Mode.INPUT)
                        .pull(PullMode.NOPULL)
                        .speed(Speed.FREQ_LOW)
                        .build(),

                GpioInit.builder()
                        .port(GPIOC)
                        .pin(GPIO_PIN_13)
                        .mode(Mode.OUTPUT_PP)
                        .pull(PullMode.NOPULL)
                        .speed(Speed.FREQ_LOW)
                        .build()
        );

        while (true) {
            ReadPinResponse response = (ReadPinResponse) commandsExecutor.sendCommand(
                    ReadPin.builder()
                            .port(GPIOB)
                            .pin(GPIO_PIN_12)
                            .build()
            );

            boolean value = response.isSet();

            commandsExecutor.sendCommand(
                    WritePin.builder().port(GPIOC).pin(GPIO_PIN_13).value(value).build()
            );

            System.out.println("B12: " + value);
            Thread.sleep(100);
        }

    }
}