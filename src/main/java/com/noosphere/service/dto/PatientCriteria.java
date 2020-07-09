package com.noosphere.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.noosphere.domain.Patient} entity. This class is used
 * in {@link com.noosphere.web.rest.PatientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullname;

    private InstantFilter dateBirthday;

    private StringFilter address;

    private StringFilter phoneNumber;

    private IntegerFilter diagnosis;

    public PatientCriteria() {
    }

    public PatientCriteria(PatientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullname = other.fullname == null ? null : other.fullname.copy();
        this.dateBirthday = other.dateBirthday == null ? null : other.dateBirthday.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.phoneNumber = other.phoneNumber == null ? null : other.phoneNumber.copy();
        this.diagnosis = other.diagnosis == null ? null : other.diagnosis.copy();
    }

    @Override
    public PatientCriteria copy() {
        return new PatientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFullname() {
        return fullname;
    }

    public void setFullname(StringFilter fullname) {
        this.fullname = fullname;
    }

    public InstantFilter getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(InstantFilter dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(StringFilter phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public IntegerFilter getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(IntegerFilter diagnosis) {
        this.diagnosis = diagnosis;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientCriteria that = (PatientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fullname, that.fullname) &&
            Objects.equals(dateBirthday, that.dateBirthday) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phoneNumber, that.phoneNumber) &&
            Objects.equals(diagnosis, that.diagnosis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fullname,
        dateBirthday,
        address,
        phoneNumber,
        diagnosis
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullname != null ? "fullname=" + fullname + ", " : "") +
                (dateBirthday != null ? "dateBirthday=" + dateBirthday + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (phoneNumber != null ? "phoneNumber=" + phoneNumber + ", " : "") +
                (diagnosis != null ? "diagnosis=" + diagnosis + ", " : "") +
            "}";
    }

}
