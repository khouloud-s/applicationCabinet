package com.myself.cabinet.service.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.myself.cabinet.domain.Medecin} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedecinDTO implements Serializable {

    @NotNull
    private UUID userUuid;

    private Long id;

    private String fullName;

    private String phone;

    private String adress;

    private Boolean isActive;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MedecinDTO)) {
            return false;
        }

        MedecinDTO medecinDTO = (MedecinDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, medecinDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedecinDTO{" +
            "userUuid='" + getUserUuid() + "'" +
            ", id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", adress='" + getAdress() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
