package com.myself.cabinet.web.rest;

import com.myself.cabinet.repository.ShiftHoraireRepository;
import com.myself.cabinet.service.ShiftHoraireQueryService;
import com.myself.cabinet.service.ShiftHoraireService;
import com.myself.cabinet.service.criteria.ShiftHoraireCriteria;
import com.myself.cabinet.service.dto.ShiftHoraireDTO;
import com.myself.cabinet.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myself.cabinet.domain.ShiftHoraire}.
 */
@RestController
@RequestMapping("/api")
public class ShiftHoraireResource {

    private final Logger log = LoggerFactory.getLogger(ShiftHoraireResource.class);

    private static final String ENTITY_NAME = "cabinetShiftHoraire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShiftHoraireService shiftHoraireService;

    private final ShiftHoraireRepository shiftHoraireRepository;

    private final ShiftHoraireQueryService shiftHoraireQueryService;

    public ShiftHoraireResource(
        ShiftHoraireService shiftHoraireService,
        ShiftHoraireRepository shiftHoraireRepository,
        ShiftHoraireQueryService shiftHoraireQueryService
    ) {
        this.shiftHoraireService = shiftHoraireService;
        this.shiftHoraireRepository = shiftHoraireRepository;
        this.shiftHoraireQueryService = shiftHoraireQueryService;
    }

    /**
     * {@code POST  /shift-horaires} : Create a new shiftHoraire.
     *
     * @param shiftHoraireDTO the shiftHoraireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shiftHoraireDTO, or with status {@code 400 (Bad Request)} if the shiftHoraire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shift-horaires")
    public ResponseEntity<ShiftHoraireDTO> createShiftHoraire(@Valid @RequestBody ShiftHoraireDTO shiftHoraireDTO)
        throws URISyntaxException {
        log.debug("REST request to save ShiftHoraire : {}", shiftHoraireDTO);
        if (shiftHoraireDTO.getId() != null) {
            throw new BadRequestAlertException("A new shiftHoraire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShiftHoraireDTO result = shiftHoraireService.save(shiftHoraireDTO);
        return ResponseEntity
            .created(new URI("/api/shift-horaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shift-horaires/:id} : Updates an existing shiftHoraire.
     *
     * @param id the id of the shiftHoraireDTO to save.
     * @param shiftHoraireDTO the shiftHoraireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shiftHoraireDTO,
     * or with status {@code 400 (Bad Request)} if the shiftHoraireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shiftHoraireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shift-horaires/{id}")
    public ResponseEntity<ShiftHoraireDTO> updateShiftHoraire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ShiftHoraireDTO shiftHoraireDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ShiftHoraire : {}, {}", id, shiftHoraireDTO);
        if (shiftHoraireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shiftHoraireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shiftHoraireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ShiftHoraireDTO result = shiftHoraireService.update(shiftHoraireDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shiftHoraireDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /shift-horaires/:id} : Partial updates given fields of an existing shiftHoraire, field will ignore if it is null
     *
     * @param id the id of the shiftHoraireDTO to save.
     * @param shiftHoraireDTO the shiftHoraireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shiftHoraireDTO,
     * or with status {@code 400 (Bad Request)} if the shiftHoraireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the shiftHoraireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the shiftHoraireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/shift-horaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ShiftHoraireDTO> partialUpdateShiftHoraire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ShiftHoraireDTO shiftHoraireDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ShiftHoraire partially : {}, {}", id, shiftHoraireDTO);
        if (shiftHoraireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, shiftHoraireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!shiftHoraireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ShiftHoraireDTO> result = shiftHoraireService.partialUpdate(shiftHoraireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, shiftHoraireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /shift-horaires} : get all the shiftHoraires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shiftHoraires in body.
     */
    @GetMapping("/shift-horaires")
    public ResponseEntity<List<ShiftHoraireDTO>> getAllShiftHoraires(
        ShiftHoraireCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get ShiftHoraires by criteria: {}", criteria);
        Page<ShiftHoraireDTO> page = shiftHoraireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shift-horaires/count} : count all the shiftHoraires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/shift-horaires/count")
    public ResponseEntity<Long> countShiftHoraires(ShiftHoraireCriteria criteria) {
        log.debug("REST request to count ShiftHoraires by criteria: {}", criteria);
        return ResponseEntity.ok().body(shiftHoraireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shift-horaires/:id} : get the "id" shiftHoraire.
     *
     * @param id the id of the shiftHoraireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shiftHoraireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shift-horaires/{id}")
    public ResponseEntity<ShiftHoraireDTO> getShiftHoraire(@PathVariable Long id) {
        log.debug("REST request to get ShiftHoraire : {}", id);
        Optional<ShiftHoraireDTO> shiftHoraireDTO = shiftHoraireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shiftHoraireDTO);
    }

    /**
     * {@code DELETE  /shift-horaires/:id} : delete the "id" shiftHoraire.
     *
     * @param id the id of the shiftHoraireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shift-horaires/{id}")
    public ResponseEntity<Void> deleteShiftHoraire(@PathVariable Long id) {
        log.debug("REST request to delete ShiftHoraire : {}", id);
        shiftHoraireService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
