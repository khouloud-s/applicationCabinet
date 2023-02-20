package com.myself.cabinet.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.myself.cabinet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AppointementDTO.class);
        AppointementDTO appointementDTO1 = new AppointementDTO();
        appointementDTO1.setId(1L);
        AppointementDTO appointementDTO2 = new AppointementDTO();
        assertThat(appointementDTO1).isNotEqualTo(appointementDTO2);
        appointementDTO2.setId(appointementDTO1.getId());
        assertThat(appointementDTO1).isEqualTo(appointementDTO2);
        appointementDTO2.setId(2L);
        assertThat(appointementDTO1).isNotEqualTo(appointementDTO2);
        appointementDTO1.setId(null);
        assertThat(appointementDTO1).isNotEqualTo(appointementDTO2);
    }
}
