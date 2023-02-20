package com.myself.cabinet.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ShiftHoraire.
 */
@Entity
@Table(name = "shifthoraire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ShiftHoraire implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "time_start")
    private ZonedDateTime timeStart;

    @Column(name = "time_end")
    private ZonedDateTime timeEnd;

    @OneToMany(mappedBy = "shiftHoraire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "medecin", "patient", "shiftHoraire" }, allowSetters = true)
    private Set<Appointement> appointements = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public UUID getUserUuid() {
        return this.userUuid;
    }

    public ShiftHoraire userUuid(UUID userUuid) {
        this.setUserUuid(userUuid);
        return this;
    }

    public void setUserUuid(UUID userUuid) {
        this.userUuid = userUuid;
    }

    public Long getId() {
        return this.id;
    }

    public ShiftHoraire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getTimeStart() {
        return this.timeStart;
    }

    public ShiftHoraire timeStart(ZonedDateTime timeStart) {
        this.setTimeStart(timeStart);
        return this;
    }

    public void setTimeStart(ZonedDateTime timeStart) {
        this.timeStart = timeStart;
    }

    public ZonedDateTime getTimeEnd() {
        return this.timeEnd;
    }

    public ShiftHoraire timeEnd(ZonedDateTime timeEnd) {
        this.setTimeEnd(timeEnd);
        return this;
    }

    public void setTimeEnd(ZonedDateTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Set<Appointement> getAppointements() {
        return this.appointements;
    }

    public void setAppointements(Set<Appointement> appointements) {
        if (this.appointements != null) {
            this.appointements.forEach(i -> i.setShiftHoraire(null));
        }
        if (appointements != null) {
            appointements.forEach(i -> i.setShiftHoraire(this));
        }
        this.appointements = appointements;
    }

    public ShiftHoraire appointements(Set<Appointement> appointements) {
        this.setAppointements(appointements);
        return this;
    }

    public ShiftHoraire addAppointements(Appointement appointement) {
        this.appointements.add(appointement);
        appointement.setShiftHoraire(this);
        return this;
    }

    public ShiftHoraire removeAppointements(Appointement appointement) {
        this.appointements.remove(appointement);
        appointement.setShiftHoraire(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShiftHoraire)) {
            return false;
        }
        return id != null && id.equals(((ShiftHoraire) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShiftHoraire{" +
            "id=" + getId() +
            ", userUuid='" + getUserUuid() + "'" +
            ", timeStart='" + getTimeStart() + "'" +
            ", timeEnd='" + getTimeEnd() + "'" +
            "}";
    }
}
