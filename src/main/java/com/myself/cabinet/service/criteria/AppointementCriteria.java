package com.myself.cabinet.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myself.cabinet.domain.Appointement} entity. This class is used
 * in {@link com.myself.cabinet.web.rest.AppointementResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /appointements?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AppointementCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter userUuid;

    private LongFilter id;

    private LocalDateFilter date;

    private BooleanFilter isActive;

    private LongFilter medecinId;

    private LongFilter patientId;

    private LongFilter shiftHoraireId;

    private Boolean distinct;

    public AppointementCriteria() {}

    public AppointementCriteria(AppointementCriteria other) {
        this.userUuid = other.userUuid == null ? null : other.userUuid.copy();
        this.id = other.id == null ? null : other.id.copy();
        this.date = other.date == null ? null : other.date.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.medecinId = other.medecinId == null ? null : other.medecinId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.shiftHoraireId = other.shiftHoraireId == null ? null : other.shiftHoraireId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public AppointementCriteria copy() {
        return new AppointementCriteria(this);
    }

    public UUIDFilter getUserUuid() {
        return userUuid;
    }

    public UUIDFilter userUuid() {
        if (userUuid == null) {
            userUuid = new UUIDFilter();
        }
        return userUuid;
    }

    public void setUserUuid(UUIDFilter userUuid) {
        this.userUuid = userUuid;
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LocalDateFilter getDate() {
        return date;
    }

    public LocalDateFilter date() {
        if (date == null) {
            date = new LocalDateFilter();
        }
        return date;
    }

    public void setDate(LocalDateFilter date) {
        this.date = date;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            isActive = new BooleanFilter();
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public LongFilter getMedecinId() {
        return medecinId;
    }

    public LongFilter medecinId() {
        if (medecinId == null) {
            medecinId = new LongFilter();
        }
        return medecinId;
    }

    public void setMedecinId(LongFilter medecinId) {
        this.medecinId = medecinId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public LongFilter patientId() {
        if (patientId == null) {
            patientId = new LongFilter();
        }
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    public LongFilter getShiftHoraireId() {
        return shiftHoraireId;
    }

    public LongFilter shiftHoraireId() {
        if (shiftHoraireId == null) {
            shiftHoraireId = new LongFilter();
        }
        return shiftHoraireId;
    }

    public void setShiftHoraireId(LongFilter shiftHoraireId) {
        this.shiftHoraireId = shiftHoraireId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final AppointementCriteria that = (AppointementCriteria) o;
        return (
            Objects.equals(userUuid, that.userUuid) &&
            Objects.equals(id, that.id) &&
            Objects.equals(date, that.date) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(medecinId, that.medecinId) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(shiftHoraireId, that.shiftHoraireId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, id, date, isActive, medecinId, patientId, shiftHoraireId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AppointementCriteria{" +
            (userUuid != null ? "userUuid=" + userUuid + ", " : "") +
            (id != null ? "id=" + id + ", " : "") +
            (date != null ? "date=" + date + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (medecinId != null ? "medecinId=" + medecinId + ", " : "") +
            (patientId != null ? "patientId=" + patientId + ", " : "") +
            (shiftHoraireId != null ? "shiftHoraireId=" + shiftHoraireId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
