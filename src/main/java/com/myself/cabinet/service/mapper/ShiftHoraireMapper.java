package com.myself.cabinet.service.mapper;

import com.myself.cabinet.domain.ShiftHoraire;
import com.myself.cabinet.service.dto.ShiftHoraireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ShiftHoraire} and its DTO {@link ShiftHoraireDTO}.
 */
@Mapper(componentModel = "spring")
public interface ShiftHoraireMapper extends EntityMapper<ShiftHoraireDTO, ShiftHoraire> {}
