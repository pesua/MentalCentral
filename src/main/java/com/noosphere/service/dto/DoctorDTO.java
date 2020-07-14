package com.noosphere.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.noosphere.domain.Doctor} entity.
 */
public class DoctorDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String fullname;

    @NotNull
    private String phone;

    
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DoctorDTO)) {
            return false;
        }

        return id != null && id.equals(((DoctorDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DoctorDTO{" +
            "id=" + getId() +
            ", fullname='" + getFullname() + "'" +
            ", phone='" + getPhone() + "'" +
            "}";
    }
}
