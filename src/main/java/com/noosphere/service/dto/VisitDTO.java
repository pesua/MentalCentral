package com.noosphere.service.dto;

import java.time.ZonedDateTime;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.noosphere.domain.Visit} entity.
 */
public class VisitDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String type;

    @NotNull
    private ZonedDateTime time;

    @NotNull
    private String teraphy;


    private Long doctorId;

    private String doctorFullname;

    private Long patientId;

    private String patientFullname;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getTime() {
        return time;
    }

    public void setTime(ZonedDateTime time) {
        this.time = time;
    }

    public String getTeraphy() {
        return teraphy;
    }

    public void setTeraphy(String teraphy) {
        this.teraphy = teraphy;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }

    public String getDoctorFullname() {
        return doctorFullname;
    }

    public void setDoctorFullname(String doctorFullname) {
        this.doctorFullname = doctorFullname;
    }

    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getPatientFullname() {
        return patientFullname;
    }

    public void setPatientFullname(String patientFullname) {
        this.patientFullname = patientFullname;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VisitDTO)) {
            return false;
        }

        return id != null && id.equals(((VisitDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "VisitDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            ", time='" + getTime() + "'" +
            ", teraphy='" + getTeraphy() + "'" +
            ", doctorId=" + getDoctorId() +
            ", doctorFullname='" + getDoctorFullname() + "'" +
            ", patientId=" + getPatientId() +
            ", patientFullname='" + getPatientFullname() + "'" +
            "}";
    }
}
