package com.myself.cabinet.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myself.cabinet.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AppointementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Appointement.class);
        Appointement appointement1 = new Appointement();
        appointement1.setId(1L);
        Appointement appointement2 = new Appointement();
        appointement2.setId(appointement1.getId());
        assertThat(appointement1).isEqualTo(appointement2);
        appointement2.setId(2L);
        assertThat(appointement1).isNotEqualTo(appointement2);
        appointement1.setId(null);
        assertThat(appointement1).isNotEqualTo(appointement2);
    }
}
