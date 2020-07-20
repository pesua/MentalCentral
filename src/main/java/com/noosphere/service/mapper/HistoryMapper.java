package com.noosphere.service.mapper;


import com.noosphere.domain.*;
import com.noosphere.service.dto.HistoryDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link History} and its DTO {@link HistoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {PatientMapper.class})
public interface HistoryMapper extends EntityMapper<HistoryDTO, History> {

    @Mapping(source = "patient.id", target = "patientId")
    @Mapping(source = "patient.fullName", target = "patientFullName")
    HistoryDTO toDto(History history);

    @Mapping(source = "patientId", target = "patient")
    History toEntity(HistoryDTO historyDTO);

    default History fromId(Long id) {
        if (id == null) {
            return null;
        }
        History history = new History();
        history.setId(id);
        return history;
    }
}
