package com.myself.cabinet.repository;

import com.myself.cabinet.domain.Appointement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Appointement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AppointementRepository extends JpaRepository<Appointement, Long>, JpaSpecificationExecutor<Appointement> {}
