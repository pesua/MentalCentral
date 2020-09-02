package com.noosphere.mental_central.repository;

import com.noosphere.mental_central.domain.Visit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Visit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VisitRepository extends JpaRepository<Visit, Long>, JpaSpecificationExecutor<Visit> {

    @Query("select visit from Visit visit where visit.user.login = ?#{principal.username} order by visit.time asc")
    Page<Visit> findByUserIsCurrentUser(Pageable pageable);

    @Query("select visit from Visit visit order by visit.time asc ")
    Page<Visit> findAllByOrderByTimeDesc(Pageable pageable);
}
