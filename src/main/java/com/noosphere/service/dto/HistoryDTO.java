package com.noosphere.service.dto;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;

/**
 * A DTO for the {@link com.noosphere.domain.History} entity.
 */
public class HistoryDTO implements Serializable {
    
    private Long id;

    @NotNull
    private LocalDate dateRecord;

    @NotNull
    private String type;

    @NotNull
    private String info;


    private Long patientId;

    private String patientFullname;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateRecord() {
        return dateRecord;
    }

    public void setDateRecord(LocalDate dateRecord) {
        this.dateRecord = dateRecord;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
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
        if (!(o instanceof HistoryDTO)) {
            return false;
        }

        return id != null && id.equals(((HistoryDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HistoryDTO{" +
            "id=" + getId() +
            ", dateRecord='" + getDateRecord() + "'" +
            ", type='" + getType() + "'" +
            ", info='" + getInfo() + "'" +
            ", patientId=" + getPatientId() +
            ", patientFullname='" + getPatientFullname() + "'" +
            "}";
    }
}
