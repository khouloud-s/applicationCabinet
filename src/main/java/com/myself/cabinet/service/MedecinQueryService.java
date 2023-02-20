package com.myself.cabinet.service;

import com.myself.cabinet.domain.*; // for static metamodels
import com.myself.cabinet.domain.Medecin;
import com.myself.cabinet.repository.MedecinRepository;
import com.myself.cabinet.service.criteria.MedecinCriteria;
import com.myself.cabinet.service.dto.MedecinDTO;
import com.myself.cabinet.service.mapper.MedecinMapper;
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
 * Service for executing complex queries for {@link Medecin} entities in the database.
 * The main input is a {@link MedecinCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MedecinDTO} or a {@link Page} of {@link MedecinDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedecinQueryService extends QueryService<Medecin> {

    private final Logger log = LoggerFactory.getLogger(MedecinQueryService.class);

    private final MedecinRepository medecinRepository;

    private final MedecinMapper medecinMapper;

    public MedecinQueryService(MedecinRepository medecinRepository, MedecinMapper medecinMapper) {
        this.medecinRepository = medecinRepository;
        this.medecinMapper = medecinMapper;
    }

    /**
     * Return a {@link List} of {@link MedecinDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MedecinDTO> findByCriteria(MedecinCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medecin> specification = createSpecification(criteria);
        return medecinMapper.toDto(medecinRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MedecinDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MedecinDTO> findByCriteria(MedecinCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medecin> specification = createSpecification(criteria);
        return medecinRepository.findAll(specification, page).map(medecinMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedecinCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medecin> specification = createSpecification(criteria);
        return medecinRepository.count(specification);
    }

    /**
     * Function to convert {@link MedecinCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medecin> createSpecification(MedecinCriteria criteria) {
        Specification<Medecin> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medecin_.id));
            }
            if (criteria.getUserUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUserUuid(), Medecin_.userUuid));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Medecin_.fullName));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Medecin_.phone));
            }
            if (criteria.getAdress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdress(), Medecin_.adress));
            }
            if (criteria.getIsActive() != null) {
                specification = specification.and(buildSpecification(criteria.getIsActive(), Medecin_.isActive));
            }
            if (criteria.getAppointementsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppointementsId(),
                            root -> root.join(Medecin_.appointements, JoinType.LEFT).get(Appointement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
