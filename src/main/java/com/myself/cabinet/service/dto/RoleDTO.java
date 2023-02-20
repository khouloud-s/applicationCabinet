package com.myself.cabinet.service.dto;

import com.myself.cabinet.domain.enumeration.RoleName;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.myself.cabinet.domain.Role} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class RoleDTO implements Serializable {

    @NotNull
    private UUID userUuid;

    @NotNull
    private Long id;

    private RoleName rolename;

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

    public RoleName getRolename() {
        return rolename;
    }

    public void setRolename(RoleName rolename) {
        this.rolename = rolename;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoleDTO)) {
            return false;
        }

        RoleDTO roleDTO = (RoleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleDTO{" +
            "userUuid='" + getUserUuid() + "'" +
            ", id=" + getId() +
            ", rolename='" + getRolename() + "'" +
            "}";
    }
}
