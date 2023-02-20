package com.myself.cabinet.web.rest;

import com.myself.cabinet.repository.AppointementRepository;
import com.myself.cabinet.service.AppointementQueryService;
import com.myself.cabinet.service.AppointementService;
import com.myself.cabinet.service.criteria.AppointementCriteria;
import com.myself.cabinet.service.dto.AppointementDTO;
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
 * REST controller for managing {@link com.myself.cabinet.domain.Appointement}.
 */
@RestController
@RequestMapping("/api")
public class AppointementResource {

    private final Logger log = LoggerFactory.getLogger(AppointementResource.class);

    private static final String ENTITY_NAME = "cabinetAppointement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppointementService appointementService;

    private final AppointementRepository appointementRepository;

    private final AppointementQueryService appointementQueryService;

    public AppointementResource(
        AppointementService appointementService,
        AppointementRepository appointementRepository,
        AppointementQueryService appointementQueryService
    ) {
        this.appointementService = appointementService;
        this.appointementRepository = appointementRepository;
        this.appointementQueryService = appointementQueryService;
    }

    /**
     * {@code POST  /appointements} : Create a new appointement.
     *
     * @param appointementDTO the appointementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appointementDTO, or with status {@code 400 (Bad Request)} if the appointement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/appointements")
    public ResponseEntity<AppointementDTO> createAppointement(@Valid @RequestBody AppointementDTO appointementDTO)
        throws URISyntaxException {
        log.debug("REST request to save Appointement : {}", appointementDTO);
        if (appointementDTO.getId() != null) {
            throw new BadRequestAlertException("A new appointement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AppointementDTO result = appointementService.save(appointementDTO);
        return ResponseEntity
            .created(new URI("/api/appointements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /appointements/:id} : Updates an existing appointement.
     *
     * @param id the id of the appointementDTO to save.
     * @param appointementDTO the appointementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointementDTO,
     * or with status {@code 400 (Bad Request)} if the appointementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appointementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/appointements/{id}")
    public ResponseEntity<AppointementDTO> updateAppointement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppointementDTO appointementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Appointement : {}, {}", id, appointementDTO);
        if (appointementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AppointementDTO result = appointementService.update(appointementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appointementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /appointements/:id} : Partial updates given fields of an existing appointement, field will ignore if it is null
     *
     * @param id the id of the appointementDTO to save.
     * @param appointementDTO the appointementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appointementDTO,
     * or with status {@code 400 (Bad Request)} if the appointementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the appointementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the appointementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/appointements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppointementDTO> partialUpdateAppointement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppointementDTO appointementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Appointement partially : {}, {}", id, appointementDTO);
        if (appointementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appointementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appointementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppointementDTO> result = appointementService.partialUpdate(appointementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, appointementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /appointements} : get all the appointements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appointements in body.
     */
    @GetMapping("/appointements")
    public ResponseEntity<List<AppointementDTO>> getAllAppointements(
        AppointementCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Appointements by criteria: {}", criteria);
        Page<AppointementDTO> page = appointementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /appointements/count} : count all the appointements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/appointements/count")
    public ResponseEntity<Long> countAppointements(AppointementCriteria criteria) {
        log.debug("REST request to count Appointements by criteria: {}", criteria);
        return ResponseEntity.ok().body(appointementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /appointements/:id} : get the "id" appointement.
     *
     * @param id the id of the appointementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appointementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/appointements/{id}")
    public ResponseEntity<AppointementDTO> getAppointement(@PathVariable Long id) {
        log.debug("REST request to get Appointement : {}", id);
        Optional<AppointementDTO> appointementDTO = appointementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(appointementDTO);
    }

    /**
     * {@code DELETE  /appointements/:id} : delete the "id" appointement.
     *
     * @param id the id of the appointementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/appointements/{id}")
    public ResponseEntity<Void> deleteAppointement(@PathVariable Long id) {
        log.debug("REST request to delete Appointement : {}", id);
        appointementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
