package com.myself.cabinet.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShiftHoraireMapperTest {

    private ShiftHoraireMapper shiftHoraireMapper;

    @BeforeEach
    public void setUp() {
        shiftHoraireMapper = new ShiftHoraireMapperImpl();
    }
}
