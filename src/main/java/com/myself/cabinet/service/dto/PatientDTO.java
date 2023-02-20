package com.myself.cabinet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.myself.cabinet.domain.Patient} entity.
 */
public class PatientDTO implements Serializable {

    @NotNull
    private UUID userUuid;

    private Long id;

    @Min(value = 5)
    @Max(value = 30)
    private String fullName;

    private String email;

    @Lob
    private byte[] scanOrdonnance;

    private String scanOrdonnanceContentType;

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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getScanOrdonnance() {
        return scanOrdonnance;
    }

    public void setScanOrdonnance(byte[] scanOrdonnance) {
        this.scanOrdonnance = scanOrdonnance;
    }

    public String getScanOrdonnanceContentType() {
        return scanOrdonnanceContentType;
    }

    public void setScanOrdonnanceContentType(String scanOrdonnanceContentType) {
        this.scanOrdonnanceContentType = scanOrdonnanceContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientDTO)) {
            return false;
        }

        PatientDTO patientDTO = (PatientDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, patientDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDTO{" +
            "userUuid='" + getUserUuid() + "'" +
            ", id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", email='" + getEmail() + "'" +
            ", scanOrdonnance='" + getScanOrdonnance() + "'" +
            "}";
    }
}
