package com.example.genericio.command;

import com.example.genericio.command.GPIO.Pin;
import lombok.Builder;

import static com.example.genericio.command.GPIO.Port;

@Builder
public class WritePin extends GenericCommand {

    private final Port port;

    private final Pin pin;

    private final Boolean value;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.WRITE_PIN.ordinal())
                .addByte(port.value)
                .addShort(pin.value)
                .addByte(value ? 1 : 0);

        return wrapContentAndGetBytes();
    }
}
