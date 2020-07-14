package com.noosphere.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.noosphere.domain.Patient} entity.
 */
public class PatientDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String fullname;

    @NotNull
    private LocalDate dateBirthday;

    @NotNull
    private String address;

    @NotNull
    private String phone;

    @NotNull
    private Integer diagnosis;

    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public LocalDate getDateBirthday() {
        return dateBirthday;
    }

    public void setDateBirthday(LocalDate dateBirthday) {
        this.dateBirthday = dateBirthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Integer diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientDTO)) {
            return false;
        }

        return id != null && id.equals(((PatientDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientDTO{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            ", dateBirthday='" + getDateBirthday() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", diagnosis=" + getDiagnosis() +
            "}";
    }
}
