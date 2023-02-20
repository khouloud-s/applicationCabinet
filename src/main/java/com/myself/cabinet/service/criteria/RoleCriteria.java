package com.myself.cabinet.service.criteria;

import com.myself.cabinet.domain.enumeration.RoleName;
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

/**
 * Criteria class for the {@link com.myself.cabinet.domain.Role} entity. This class is used
 * in {@link com.myself.cabinet.web.rest.RoleResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /roles?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
public class RoleCriteria implements Serializable, Criteria {

    /**
     * Class for filtering RoleName
     */
    public static class RoleNameFilter extends Filter<RoleName> {

        public RoleNameFilter() {}

        public RoleNameFilter(RoleNameFilter filter) {
            super(filter);
        }

        @Override
        public RoleNameFilter copy() {
            return new RoleNameFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private UUIDFilter userUuid;

    private LongFilter id;

    private RoleNameFilter rolename;

    private Boolean distinct;

    public RoleCriteria() {}

    public RoleCriteria(RoleCriteria other) {
        this.userUuid = other.userUuid == null ? null : other.userUuid.copy();
        this.id = other.id == null ? null : other.id.copy();
        this.rolename = other.rolename == null ? null : other.rolename.copy();
        this.distinct = other.distinct;
    }

    @Override
    public RoleCriteria copy() {
        return new RoleCriteria(this);
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

    public RoleNameFilter getRolename() {
        return rolename;
    }

    public RoleNameFilter rolename() {
        if (rolename == null) {
            rolename = new RoleNameFilter();
        }
        return rolename;
    }

    public void setRolename(RoleNameFilter rolename) {
        this.rolename = rolename;
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
        final RoleCriteria that = (RoleCriteria) o;
        return (
            Objects.equals(userUuid, that.userUuid) &&
            Objects.equals(id, that.id) &&
            Objects.equals(rolename, that.rolename) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(userUuid, id, rolename, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoleCriteria{" +
            (userUuid != null ? "userUuid=" + userUuid + ", " : "") +
            (id != null ? "id=" + id + ", " : "") +
            (rolename != null ? "rolename=" + rolename + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
