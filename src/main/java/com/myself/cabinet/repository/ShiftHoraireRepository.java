package com.myself.cabinet.repository;

import com.myself.cabinet.domain.ShiftHoraire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ShiftHoraire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShiftHoraireRepository extends JpaRepository<ShiftHoraire, Long>, JpaSpecificationExecutor<ShiftHoraire> {}
