package com.example.genericio.command;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BytesBuilderTest {

    @Test
    void getBytes_after_addByte() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addByte(123);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{123});
    }

    @Test
    void getBytes_after_addByte_high_value() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addByte(0xff);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{-1});
    }

    @Test
    void getBytes_after_addShort() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addShort(0x1234);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{0x34, 0x12});
    }

    @Test
    void getBytes_after_addShort_high_value() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addShort(0xffff);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{-1, -1});
    }

    @Test
    void getBytes_after_addInt() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addInt(0x12345678);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{0x78, 0x56, 0x34, 0x12});
    }

    @Test
    void getBytes_after_addInt_high_value() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addInt(0xffffffff);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{-1, -1, -1, -1});
    }

    @Test
    void getSize_of_after_addByte_and_addShort_and_addInt() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addByte(0xff)
                .addShort(0x1234)
                .addInt(0x12345678);

        // then
        assertThat(command.getSize())
                .isEqualTo(1 + 2 + 4);
    }

    @Test
    void getBytes_after_addByteFirst() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addShort(0xffff)
                .addByteFirst(0x12);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{0x12, -1, -1});
    }

    @Test
    void getBytes_after_addShortFirst() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addShort(0xffff)
                .addShortFirst(0x1234);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{0x34, 0x12, -1, -1});
    }

    @Test
    void getBytes_after_addIntFirst() {
        // given
        BytesBuilder command = new BytesBuilder();

        // when
        command.addShort(0xffff)
                .addIntFirst(0x12345678);

        // then
        assertThat(command.getBytes())
                .isEqualTo(new byte[]{0x78, 0x56, 0x34, 0x12, -1, -1});
    }
}