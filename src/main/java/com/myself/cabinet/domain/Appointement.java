package com.myself.cabinet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Cabinet
 */
@Entity
@Table(name = "appointement")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Appointement implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "is_active")
    private Boolean isActive;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appointements" }, allowSetters = true)
    private Medecin medecin;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appointements" }, allowSetters = true)
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "appointements" }, allowSetters = true)
    private ShiftHoraire shiftHoraire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getUserUuid() {
        return this.userUuid;
    }

    public Appointement userUuid(UUID userUuid) {
        this.setUserUuid(userUuid);
        return this;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Long getId() {
        return this.id;
    }

    public Appointement id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Appointement date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Appointement isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Medecin getMedecin() {
        return this.medecin;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Appointement medecin(Medecin medecin) {
        this.setMedecin(medecin);
        return this;
    }

    public Patient getPatient() {
        return this.patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Appointement patient(Patient patient) {
        this.setPatient(patient);
        return this;
    }

    public ShiftHoraire getShiftHoraire() {
        return this.shiftHoraire;
    }

    public void setShiftHoraire(ShiftHoraire shiftHoraire) {
        this.shiftHoraire = shiftHoraire;
    }

    public Appointement shiftHoraire(ShiftHoraire shiftHoraire) {
        this.setShiftHoraire(shiftHoraire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Appointement)) {
            return false;
        }
        return id != null && id.equals(((Appointement) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Appointement{" +
            "id=" + getId() +
            ", userUuid='" + getUserUuid() + "'" +
            ", date='" + getDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
