package com.myself.cabinet.service.mapper;

import com.myself.cabinet.domain.Medecin;
import com.myself.cabinet.service.dto.MedecinDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Medecin} and its DTO {@link MedecinDTO}.
 */
@Mapper(componentModel = "spring")
public interface MedecinMapper extends EntityMapper<MedecinDTO, Medecin> {}
