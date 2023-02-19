package com.myself.cabinet.service;

import com.myself.cabinet.domain.*; // for static metamodels
import com.myself.cabinet.domain.Appointement;
import com.myself.cabinet.repository.AppointementRepository;
import com.myself.cabinet.service.criteria.AppointementCriteria;
import com.myself.cabinet.service.dto.AppointementDTO;
import com.myself.cabinet.service.mapper.AppointementMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Appointement} entities in the database.
 * The main input is a {@link AppointementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AppointementDTO} or a {@link Page} of {@link AppointementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AppointementQueryService extends QueryService<Appointement> {

    private final Logger log = LoggerFactory.getLogger(AppointementQueryService.class);

    private final AppointementRepository appointementRepository;

    private final AppointementMapper appointementMapper;

    public AppointementQueryService(AppointementRepository appointementRepository, AppointementMapper appointementMapper) {
        this.appointementRepository = appointementRepository;
        this.appointementMapper = appointementMapper;
    }

    /**
     * Return a {@link List} of {@link AppointementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AppointementDTO> findByCriteria(AppointementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Appointement> specification = createSpecification(criteria);
        return appointementMapper.toDto(appointementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AppointementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointementDTO> findByCriteria(AppointementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Appointement> specification = createSpecification(criteria);
        return appointementRepository.findAll(specification, page).map(appointementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AppointementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Appointement> specification = createSpecification(criteria);
        return appointementRepository.count(specification);
    }

    /**
     * Function to convert {@link AppointementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Appointement> createSpecification(AppointementCriteria criteria) {
        Specification<Appointement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Appointement_.id));
            }
            if (criteria.getUserUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUserUuid(), Appointement_.userUuid));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDate(), Appointement_.date));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Appointement_.isActive));
            }
            if (criteria.getMedecinId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMedecinId(),
                            root -> root.join(Appointement_.medecin, JoinType.LEFT).get(Medecin_.id)
                        )
                    );
            }
            if (criteria.getPatientId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPatientId(),
                            root -> root.join(Appointement_.patient, JoinType.LEFT).get(Patient_.id)
                        )
                    );
            }
            if (criteria.getShiftHoraireId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getShiftHoraireId(),
                            root -> root.join(Appointement_.shiftHoraire, JoinType.LEFT).get(ShiftHoraire_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
