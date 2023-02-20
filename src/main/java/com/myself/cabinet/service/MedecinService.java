package com.myself.cabinet.service;

import com.myself.cabinet.domain.Medecin;
import com.myself.cabinet.repository.MedecinRepository;
import com.myself.cabinet.service.dto.MedecinDTO;
import com.myself.cabinet.service.mapper.MedecinMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Medecin}.
 */
@Service
@Transactional
public class MedecinService {

    private final Logger log = LoggerFactory.getLogger(MedecinService.class);

    private final MedecinRepository medecinRepository;

    private final MedecinMapper medecinMapper;

    public MedecinService(MedecinRepository medecinRepository, MedecinMapper medecinMapper) {
        this.medecinRepository = medecinRepository;
        this.medecinMapper = medecinMapper;
    }

    /**
     * Save a medecin.
     *
     * @param medecinDTO the entity to save.
     * @return the persisted entity.
     */
    public MedecinDTO save(MedecinDTO medecinDTO) {
        log.debug("Request to save Medecin : {}", medecinDTO);
        Medecin medecin = medecinMapper.toEntity(medecinDTO);
        medecin = medecinRepository.save(medecin);
        return medecinMapper.toDto(medecin);
    }

    /**
     * Update a medecin.
     *
     * @param medecinDTO the entity to save.
     * @return the persisted entity.
     */
    public MedecinDTO update(MedecinDTO medecinDTO) {
        log.debug("Request to save Medecin : {}", medecinDTO);
        Medecin medecin = medecinMapper.toEntity(medecinDTO);
        medecin = medecinRepository.save(medecin);
        return medecinMapper.toDto(medecin);
    }

    /**
     * Partially update a medecin.
     *
     * @param medecinDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MedecinDTO> partialUpdate(MedecinDTO medecinDTO) {
        log.debug("Request to partially update Medecin : {}", medecinDTO);

        return medecinRepository
            .findById(medecinDTO.getId())
            .map(existingMedecin -> {
                medecinMapper.partialUpdate(existingMedecin, medecinDTO);

                return existingMedecin;
            })
            .map(medecinRepository::save)
            .map(medecinMapper::toDto);
    }

    /**
     * Get all the medecins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MedecinDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Medecins");
        return medecinRepository.findAll(pageable).map(medecinMapper::toDto);
    }

    /**
     * Get one medecin by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MedecinDTO> findOne(Long id) {
        log.debug("Request to get Medecin : {}", id);
        return medecinRepository.findById(id).map(medecinMapper::toDto);
    }

    /**
     * Delete the medecin by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medecin : {}", id);
        medecinRepository.deleteById(id);
    }
}
