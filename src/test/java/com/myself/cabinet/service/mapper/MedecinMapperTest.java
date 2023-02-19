package com.myself.cabinet.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MedecinMapperTest {

    private MedecinMapper medecinMapper;

    @BeforeEach
    public void setUp() {
        medecinMapper = new MedecinMapperImpl();
    }
}
