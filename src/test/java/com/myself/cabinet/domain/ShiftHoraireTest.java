package com.myself.cabinet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myself.cabinet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShiftHoraireTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShiftHoraire.class);
        ShiftHoraire shiftHoraire1 = new ShiftHoraire();
        shiftHoraire1.setId(1L);
        ShiftHoraire shiftHoraire2 = new ShiftHoraire();
        shiftHoraire2.setId(shiftHoraire1.getId());
        assertThat(shiftHoraire1).isEqualTo(shiftHoraire2);
        shiftHoraire2.setId(2L);
        assertThat(shiftHoraire1).isNotEqualTo(shiftHoraire2);
        shiftHoraire1.setId(null);
        assertThat(shiftHoraire1).isNotEqualTo(shiftHoraire2);
    }
}
