package com.myself.cabinet.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.api.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.myself.cabinet.domain.Medecin} entity. This class is used
 * in {@link com.myself.cabinet.web.rest.MedecinResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medecins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MedecinCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private UUIDFilter userUuid;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter phone;

    private StringFilter adress;

    private BooleanFilter isActive;

    private LongFilter appointementsId;

    private Boolean distinct;

    public MedecinCriteria() {}

    public MedecinCriteria(MedecinCriteria other) {
        this.userUuid = other.userUuid == null ? null : other.userUuid.copy();
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.adress = other.adress == null ? null : other.adress.copy();
        this.isActive = other.isActive == null ? null : other.isActive.copy();
        this.appointementsId = other.appointementsId == null ? null : other.appointementsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public MedecinCriteria copy() {
        return new MedecinCriteria(this);
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

    public StringFilter getFullName() {
        return fullName;
    }

    public StringFilter fullName() {
        if (fullName == null) {
            fullName = new StringFilter();
        }
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public StringFilter phone() {
        if (phone == null) {
            phone = new StringFilter();
        }
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAdress() {
        return adress;
    }

    public StringFilter adress() {
        if (adress == null) {
            adress = new StringFilter();
        }
        return adress;
    }

    public void setAdress(StringFilter adress) {
        this.adress = adress;
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
        final MedecinCriteria that = (MedecinCriteria) o;
        return (
            Objects.equals(userUuid, that.userUuid) &&
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(adress, that.adress) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(appointementsId, that.appointementsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, id, fullName, phone, adress, isActive, appointementsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MedecinCriteria{" +
            (userUuid != null ? "userUuid=" + userUuid + ", " : "") +
            (id != null ? "id=" + id + ", " : "") +
            (fullName != null ? "fullName=" + fullName + ", " : "") +
            (phone != null ? "phone=" + phone + ", " : "") +
            (adress != null ? "adress=" + adress + ", " : "") +
            (isActive != null ? "isActive=" + isActive + ", " : "") +
            (appointementsId != null ? "appointementsId=" + appointementsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
