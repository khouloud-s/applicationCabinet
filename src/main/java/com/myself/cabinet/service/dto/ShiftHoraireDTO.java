package com.myself.cabinet.service.dto;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.myself.cabinet.domain.ShiftHoraire} entity.
 */
public class ShiftHoraireDTO implements Serializable {

    @NotNull
    private UUID userUuid;

    private Long id;

    private ZonedDateTime timeStart;

    private ZonedDateTime timeEnd;

    public UUID getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(ZonedDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public ZonedDateTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(ZonedDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShiftHoraireDTO)) {
            return false;
        }

        ShiftHoraireDTO shiftHoraireDTO = (ShiftHoraireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, shiftHoraireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShiftHoraireDTO{" +
            "userUuid='" + getUserUuid() + "'" +
            ", id=" + getId() +
            ", timeStart='" + getTimeStart() + "'" +
            ", timeEnd='" + getTimeEnd() + "'" +
            "}";
    }
}
