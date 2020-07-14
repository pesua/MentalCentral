package com.noosphere.service.mapper;


import com.noosphere.domain.*;
import com.noosphere.service.dto.VisitDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Visit} and its DTO {@link VisitDTO}.
 */
@Mapper(componentModel = "spring", uses = {DoctorMapper.class, PatientMapper.class})
public interface VisitMapper extends EntityMapper<VisitDTO, Visit> {

    @Mapping(source = "doctor.id", target = "doctorId")
    @Mapping(source = "doctor.fullname", target = "doctorFullname")
    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.fullname", target = "patientFullname")
    VisitDTO toDto(Visit visit);

    @Mapping(source = "doctorId", target = "doctor")
    @Mapping(source = "patientId", target = "patient")
    Visit toEntity(VisitDTO visitDTO);

    default Visit fromId(Long id) {
        if (id == null) {
            return null;
        }
        Visit visit = new Visit();
        visit.setId(id);
        return visit;
    }
}
