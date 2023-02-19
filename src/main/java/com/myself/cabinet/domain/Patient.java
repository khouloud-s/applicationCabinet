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
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 5)
    @Max(value = 30)
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Lob
    @Column(name = "scan_ordonnance")
    private byte[] scanOrdonnance;

    @Column(name = "scan_ordonnance_content_type")
    private String scanOrdonnanceContentType;

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medecin", "patient", "shiftHoraire" }, allowSetters = true)
    private Set<Appointement> appointements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getUserUuid() {
        return this.userUuid;
    }

    public Patient userUuid(UUID userUuid) {
        this.setUserUuid(userUuid);
        return this;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Long getId() {
        return this.id;
    }

    public Patient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public Patient fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return this.email;
    }

    public Patient email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public byte[] getScanOrdonnance() {
        return this.scanOrdonnance;
    }

    public Patient scanOrdonnance(byte[] scanOrdonnance) {
        this.setScanOrdonnance(scanOrdonnance);
        return this;
    }

    public void setScanOrdonnance(byte[] scanOrdonnance) {
        this.scanOrdonnance = scanOrdonnance;
    }

    public String getScanOrdonnanceContentType() {
        return this.scanOrdonnanceContentType;
    }

    public Patient scanOrdonnanceContentType(String scanOrdonnanceContentType) {
        this.scanOrdonnanceContentType = scanOrdonnanceContentType;
        return this;
    }

    public void setScanOrdonnanceContentType(String scanOrdonnanceContentType) {
        this.scanOrdonnanceContentType = scanOrdonnanceContentType;
    }

    public Set<Appointement> getAppointements() {
        return this.appointements;
    }

    public void setAppointements(Set<Appointement> appointements) {
        if (this.appointements != null) {
            this.appointements.forEach(i -> i.setPatient(null));
        }
        if (appointements != null) {
            appointements.forEach(i -> i.setPatient(this));
        }
        this.appointements = appointements;
    }

    public Patient appointements(Set<Appointement> appointements) {
        this.setAppointements(appointements);
        return this;
    }

    public Patient addAppointements(Appointement appointement) {
        this.appointements.add(appointement);
        appointement.setPatient(this);
        return this;
    }

    public Patient removeAppointements(Appointement appointement) {
        this.appointements.remove(appointement);
        appointement.setPatient(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", userUuid='" + getUserUuid() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", email='" + getEmail() + "'" +
            ", scanOrdonnance='" + getScanOrdonnance() + "'" +
            ", scanOrdonnanceContentType='" + getScanOrdonnanceContentType() + "'" +
            "}";
    }
}
