package com.myself.cabinet.service;

import com.myself.cabinet.domain.Appointement;
import com.myself.cabinet.repository.AppointementRepository;
import com.myself.cabinet.service.dto.AppointementDTO;
import com.myself.cabinet.service.mapper.AppointementMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Appointement}.
 */
@Service
@Transactional
public class AppointementService {

    private final Logger log = LoggerFactory.getLogger(AppointementService.class);

    private final AppointementRepository appointementRepository;

    private final AppointementMapper appointementMapper;

    public AppointementService(AppointementRepository appointementRepository, AppointementMapper appointementMapper) {
        this.appointementRepository = appointementRepository;
        this.appointementMapper = appointementMapper;
    }

    /**
     * Save a appointement.
     *
     * @param appointementDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointementDTO save(AppointementDTO appointementDTO) {
        log.debug("Request to save Appointement : {}", appointementDTO);
        Appointement appointement = appointementMapper.toEntity(appointementDTO);
        appointement = appointementRepository.save(appointement);
        return appointementMapper.toDto(appointement);
    }

    /**
     * Update a appointement.
     *
     * @param appointementDTO the entity to save.
     * @return the persisted entity.
     */
    public AppointementDTO update(AppointementDTO appointementDTO) {
        log.debug("Request to update Appointement : {}", appointementDTO);
        Appointement appointement = appointementMapper.toEntity(appointementDTO);
        appointement = appointementRepository.save(appointement);
        return appointementMapper.toDto(appointement);
    }

    /**
     * Partially update a appointement.
     *
     * @param appointementDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AppointementDTO> partialUpdate(AppointementDTO appointementDTO) {
        log.debug("Request to partially update Appointement : {}", appointementDTO);

        return appointementRepository
            .findById(appointementDTO.getId())
            .map(existingAppointement -> {
                appointementMapper.partialUpdate(existingAppointement, appointementDTO);

                return existingAppointement;
            })
            .map(appointementRepository::save)
            .map(appointementMapper::toDto);
    }

    /**
     * Get all the appointements.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AppointementDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Appointements");
        return appointementRepository.findAll(pageable).map(appointementMapper::toDto);
    }

    /**
     * Get one appointement by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AppointementDTO> findOne(Long id) {
        log.debug("Request to get Appointement : {}", id);
        return appointementRepository.findById(id).map(appointementMapper::toDto);
    }

    /**
     * Delete the appointement by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Appointement : {}", id);
        appointementRepository.deleteById(id);
    }
}
