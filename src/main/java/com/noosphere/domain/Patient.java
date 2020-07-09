package com.noosphere.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "fullname", nullable = false)
    private String fullname;

    @NotNull
    @Column(name = "date_birthday", nullable = false)
    private Instant dateBirthday;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NotNull
    @Column(name = "diagnosis", nullable = false)
    private Integer diagnosis;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public Patient fullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Instant getDateBirthday() {
        return dateBirthday;
    }

    public Patient dateBirthday(Instant dateBirthday) {
        this.dateBirthday = dateBirthday;
        return this;
    }

    public void setDateBirthday(Instant dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public String getAddress() {
        return address;
    }

    public Patient address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Patient phoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getDiagnosis() {
        return diagnosis;
    }

    public Patient diagnosis(Integer diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public void setDiagnosis(Integer diagnosis) {
        this.diagnosis = diagnosis;
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
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            ", dateBirthday='" + getDateBirthday() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", diagnosis=" + getDiagnosis() +
            "}";
    }
}
