package com.noosphere.mental_central.repository;

import com.noosphere.mental_central.domain.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    Optional<List<Patient>> findAllByFullNameIgnoreCase(String fullName);
}
