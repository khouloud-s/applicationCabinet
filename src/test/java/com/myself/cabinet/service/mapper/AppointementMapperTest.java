package com.myself.cabinet.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointementMapperTest {

    private AppointementMapper appointementMapper;

    @BeforeEach
    public void setUp() {
        appointementMapper = new AppointementMapperImpl();
    }
}
