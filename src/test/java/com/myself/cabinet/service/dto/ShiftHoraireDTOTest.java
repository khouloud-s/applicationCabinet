package com.myself.cabinet.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myself.cabinet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ShiftHoraireDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShiftHoraireDTO.class);
        ShiftHoraireDTO shiftHoraireDTO1 = new ShiftHoraireDTO();
        shiftHoraireDTO1.setId(1L);
        ShiftHoraireDTO shiftHoraireDTO2 = new ShiftHoraireDTO();
        assertThat(shiftHoraireDTO1).isNotEqualTo(shiftHoraireDTO2);
        shiftHoraireDTO2.setId(shiftHoraireDTO1.getId());
        assertThat(shiftHoraireDTO1).isEqualTo(shiftHoraireDTO2);
        shiftHoraireDTO2.setId(2L);
        assertThat(shiftHoraireDTO1).isNotEqualTo(shiftHoraireDTO2);
        shiftHoraireDTO1.setId(null);
        assertThat(shiftHoraireDTO1).isNotEqualTo(shiftHoraireDTO2);
    }
}
