package com.myself.cabinet.repository;

import com.myself.cabinet.domain.Medecin;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Medecin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long>, JpaSpecificationExecutor<Medecin> {}
