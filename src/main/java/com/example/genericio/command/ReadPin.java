package com.example.genericio.command;

import lombok.Builder;

import static com.example.genericio.command.GPIO.Pin;
import static com.example.genericio.command.GPIO.Port;

@Builder
public class ReadPin extends GenericCommand {

    private final Port port;

    private final Pin pin;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.READ_PIN.ordinal())
                .addByte(port.value)
                .addShort(pin.value);

        return wrapContentAndGetBytes();
    }

}
