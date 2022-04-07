package com.example.genericio.command;

import com.example.genericio.command.GPIO.Pin;
import lombok.Builder;

import static com.example.genericio.command.GPIO.Port;

@Builder
public class WritePin extends GenericCommand {

    private Port port;

    private Pin pin;

    private Boolean value;

    @Override
    public byte[] getBytes() {
        bytesBuilder.addShort(CommandIds.WRITE_PIN.ordinal())
                .addByte(port.value)
                .addShort(pin.value)
                .addByte(value ? 1 : 0);

        return wrapContentAndGetBytes();
    }
}
