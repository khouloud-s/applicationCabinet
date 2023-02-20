package com.myself.cabinet.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;
import tech.jhipster.service.filter.UUIDFilter;
import tech.jhipster.service.filter.ZonedDateTimeFilter;

/**
 * Criteria class for the {@link com.myself.cabinet.domain.ShiftHoraire} entity. This class is used
 * in {@link com.myself.cabinet.web.rest.ShiftHoraireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shift-horaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class ShiftHoraireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter userUuid;

    private LongFilter id;

    private ZonedDateTimeFilter timeStart;

    private ZonedDateTimeFilter timeEnd;

    private LongFilter appointementsId;

    private Boolean distinct;

    public ShiftHoraireCriteria() {}

    public ShiftHoraireCriteria(ShiftHoraireCriteria other) {
        this.userUuid = other.userUuid == null ? null : other.userUuid.copy();
        this.id = other.id == null ? null : other.id.copy();
        this.timeStart = other.timeStart == null ? null : other.timeStart.copy();
        this.timeEnd = other.timeEnd == null ? null : other.timeEnd.copy();
        this.appointementsId = other.appointementsId == null ? null : other.appointementsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ShiftHoraireCriteria copy() {
        return new ShiftHoraireCriteria(this);
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

    public ZonedDateTimeFilter getTimeStart() {
        return timeStart;
    }

    public ZonedDateTimeFilter timeStart() {
        if (timeStart == null) {
            timeStart = new ZonedDateTimeFilter();
        }
        return timeStart;
    }

    public void setTimeStart(ZonedDateTimeFilter timeStart) {
        this.timeStart = timeStart;
    }

    public ZonedDateTimeFilter getTimeEnd() {
        return timeEnd;
    }

    public ZonedDateTimeFilter timeEnd() {
        if (timeEnd == null) {
            timeEnd = new ZonedDateTimeFilter();
        }
        return timeEnd;
    }

    public void setTimeEnd(ZonedDateTimeFilter timeEnd) {
        this.timeEnd = timeEnd;
    }

    public LongFilter getAppointementsId() {
        return appointementsId;
    }

    public LongFilter appointementsId() {
        if (appointementsId == null) {
            appointementsId = new LongFilter();
        }
        return appointementsId;
    }

    public void setAppointementsId(LongFilter appointementsId) {
        this.appointementsId = appointementsId;
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
        final ShiftHoraireCriteria that = (ShiftHoraireCriteria) o;
        return (
            Objects.equals(userUuid, that.userUuid) &&
            Objects.equals(id, that.id) &&
            Objects.equals(timeStart, that.timeStart) &&
            Objects.equals(timeEnd, that.timeEnd) &&
            Objects.equals(appointementsId, that.appointementsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, id, timeStart, timeEnd, appointementsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ShiftHoraireCriteria{" +
            (userUuid != null ? "userUuid=" + userUuid + ", " : "") +
            (id != null ? "id=" + id + ", " : "") +
            (timeStart != null ? "timeStart=" + timeStart + ", " : "") +
            (timeEnd != null ? "timeEnd=" + timeEnd + ", " : "") +
            (appointementsId != null ? "appointementsId=" + appointementsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
