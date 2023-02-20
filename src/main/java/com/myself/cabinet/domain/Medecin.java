package com.myself.cabinet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Medecin.
 */
@Entity
@Table(name = "medecin")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Medecin implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "phone")
    private String phone;

    @Column(name = "adress")
    private String adress;

    @Column(name = "is_active")
    private Boolean isActive;

    @OneToMany(mappedBy = "medecin")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medecin", "patient", "shiftHoraire" }, allowSetters = true)
    private Set<Appointement> appointements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getUserUuid() {
        return this.userUuid;
    }

    public Medecin userUuid(UUID userUuid) {
        this.setUserUuid(userUuid);
        return this;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Long getId() {
        return this.id;
    }

    public Medecin id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Medecin fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return this.phone;
    }

    public Medecin phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return this.adress;
    }

    public Medecin adress(String adress) {
        this.setAdress(adress);
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Medecin isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Appointement> getAppointements() {
        return this.appointements;
    }

    public void setAppointements(Set<Appointement> appointements) {
        if (this.appointements != null) {
            this.appointements.forEach(i -> i.setMedecin(null));
        }
        if (appointements != null) {
            appointements.forEach(i -> i.setMedecin(this));
        }
        this.appointements = appointements;
    }

    public Medecin appointements(Set<Appointement> appointements) {
        this.setAppointements(appointements);
        return this;
    }

    public Medecin addAppointements(Appointement appointement) {
        this.appointements.add(appointement);
        appointement.setMedecin(this);
        return this;
    }

    public Medecin removeAppointements(Appointement appointement) {
        this.appointements.remove(appointement);
        appointement.setMedecin(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medecin)) {
            return false;
        }
        return id != null && id.equals(((Medecin) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Medecin{" +
            "id=" + getId() +
            ", userUuid='" + getUserUuid() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", adress='" + getAdress() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
