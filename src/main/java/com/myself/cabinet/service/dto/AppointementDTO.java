package com.myself.cabinet.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.myself.cabinet.domain.Appointement} entity.
 */
@Schema(description = "Cabinet")
public class AppointementDTO implements Serializable {

    @NotNull
    private UUID userUuid;

    private Long id;

    private LocalDate date;

    private Boolean isActive;

    private MedecinDTO medecin;

    private PatientDTO patient;

    private ShiftHoraireDTO shiftHoraire;

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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public MedecinDTO getMedecin() {
        return medecin;
    }

    public void setMedecin(MedecinDTO medecin) {
        this.medecin = medecin;
    }

    public PatientDTO getPatient() {
        return patient;
    }

    public void setPatient(PatientDTO patient) {
        this.patient = patient;
    }

    public ShiftHoraireDTO getShiftHoraire() {
        return shiftHoraire;
    }

    public void setShiftHoraire(ShiftHoraireDTO shiftHoraire) {
        this.shiftHoraire = shiftHoraire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppointementDTO)) {
            return false;
        }

        AppointementDTO appointementDTO = (AppointementDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, appointementDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointementDTO{" +
            "userUuid='" + getUserUuid() + "'" +
            ", id=" + getId() +
            ", date='" + getDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            ", medecin=" + getMedecin() +
            ", patient=" + getPatient() +
            ", shiftHoraire=" + getShiftHoraire() +
            "}";
    }
}
