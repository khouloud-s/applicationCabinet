package com.myself.cabinet.service;

import com.myself.cabinet.domain.ShiftHoraire;
import com.myself.cabinet.repository.ShiftHoraireRepository;
import com.myself.cabinet.service.dto.ShiftHoraireDTO;
import com.myself.cabinet.service.mapper.ShiftHoraireMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ShiftHoraire}.
 */
@Service
@Transactional
public class ShiftHoraireService {

    private final Logger log = LoggerFactory.getLogger(ShiftHoraireService.class);

    private final ShiftHoraireRepository shiftHoraireRepository;

    private final ShiftHoraireMapper shiftHoraireMapper;

    public ShiftHoraireService(ShiftHoraireRepository shiftHoraireRepository, ShiftHoraireMapper shiftHoraireMapper) {
        this.shiftHoraireRepository = shiftHoraireRepository;
        this.shiftHoraireMapper = shiftHoraireMapper;
    }

    /**
     * Save a shiftHoraire.
     *
     * @param shiftHoraireDTO the entity to save.
     * @return the persisted entity.
     */
    public ShiftHoraireDTO save(ShiftHoraireDTO shiftHoraireDTO) {
        log.debug("Request to save ShiftHoraire : {}", shiftHoraireDTO);
        ShiftHoraire shiftHoraire = shiftHoraireMapper.toEntity(shiftHoraireDTO);
        shiftHoraire = shiftHoraireRepository.save(shiftHoraire);
        return shiftHoraireMapper.toDto(shiftHoraire);
    }

    /**
     * Update a shiftHoraire.
     *
     * @param shiftHoraireDTO the entity to save.
     * @return the persisted entity.
     */
    public ShiftHoraireDTO update(ShiftHoraireDTO shiftHoraireDTO) {
        log.debug("Request to update ShiftHoraire : {}", shiftHoraireDTO);
        ShiftHoraire shiftHoraire = shiftHoraireMapper.toEntity(shiftHoraireDTO);
        shiftHoraire = shiftHoraireRepository.save(shiftHoraire);
        return shiftHoraireMapper.toDto(shiftHoraire);
    }

    /**
     * Partially update a shiftHoraire.
     *
     * @param shiftHoraireDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ShiftHoraireDTO> partialUpdate(ShiftHoraireDTO shiftHoraireDTO) {
        log.debug("Request to partially update ShiftHoraire : {}", shiftHoraireDTO);

        return shiftHoraireRepository
            .findById(shiftHoraireDTO.getId())
            .map(existingShiftHoraire -> {
                shiftHoraireMapper.partialUpdate(existingShiftHoraire, shiftHoraireDTO);

                return existingShiftHoraire;
            })
            .map(shiftHoraireRepository::save)
            .map(shiftHoraireMapper::toDto);
    }

    /**
     * Get all the shiftHoraires.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShiftHoraireDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShiftHoraires");
        return shiftHoraireRepository.findAll(pageable).map(shiftHoraireMapper::toDto);
    }

    /**
     * Get one shiftHoraire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShiftHoraireDTO> findOne(Long id) {
        log.debug("Request to get ShiftHoraire : {}", id);
        return shiftHoraireRepository.findById(id).map(shiftHoraireMapper::toDto);
    }

    /**
     * Delete the shiftHoraire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShiftHoraire : {}", id);
        shiftHoraireRepository.deleteById(id);
    }
}
