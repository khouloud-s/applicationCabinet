package com.myself.cabinet.service;

import com.myself.cabinet.domain.*; // for static metamodels
import com.myself.cabinet.domain.ShiftHoraire;
import com.myself.cabinet.repository.ShiftHoraireRepository;
import com.myself.cabinet.service.criteria.ShiftHoraireCriteria;
import com.myself.cabinet.service.dto.ShiftHoraireDTO;
import com.myself.cabinet.service.mapper.ShiftHoraireMapper;
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
 * Service for executing complex queries for {@link ShiftHoraire} entities in the database.
 * The main input is a {@link ShiftHoraireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShiftHoraireDTO} or a {@link Page} of {@link ShiftHoraireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShiftHoraireQueryService extends QueryService<ShiftHoraire> {

    private final Logger log = LoggerFactory.getLogger(ShiftHoraireQueryService.class);

    private final ShiftHoraireRepository shiftHoraireRepository;

    private final ShiftHoraireMapper shiftHoraireMapper;

    public ShiftHoraireQueryService(ShiftHoraireRepository shiftHoraireRepository, ShiftHoraireMapper shiftHoraireMapper) {
        this.shiftHoraireRepository = shiftHoraireRepository;
        this.shiftHoraireMapper = shiftHoraireMapper;
    }

    /**
     * Return a {@link List} of {@link ShiftHoraireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShiftHoraireDTO> findByCriteria(ShiftHoraireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShiftHoraire> specification = createSpecification(criteria);
        return shiftHoraireMapper.toDto(shiftHoraireRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShiftHoraireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShiftHoraireDTO> findByCriteria(ShiftHoraireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShiftHoraire> specification = createSpecification(criteria);
        return shiftHoraireRepository.findAll(specification, page).map(shiftHoraireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShiftHoraireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShiftHoraire> specification = createSpecification(criteria);
        return shiftHoraireRepository.count(specification);
    }

    /**
     * Function to convert {@link ShiftHoraireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShiftHoraire> createSpecification(ShiftHoraireCriteria criteria) {
        Specification<ShiftHoraire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShiftHoraire_.id));
            }
            if (criteria.getUserUuid() != null) {
                specification = specification.and(buildSpecification(criteria.getUserUuid(), ShiftHoraire_.userUuid));
            }
            if (criteria.getTimeStart() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeStart(), ShiftHoraire_.timeStart));
            }
            if (criteria.getTimeEnd() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeEnd(), ShiftHoraire_.timeEnd));
            }
            if (criteria.getAppointementsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAppointementsId(),
                            root -> root.join(ShiftHoraire_.appointements, JoinType.LEFT).get(Appointement_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
