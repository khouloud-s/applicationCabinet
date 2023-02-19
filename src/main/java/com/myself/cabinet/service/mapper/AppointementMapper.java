package com.myself.cabinet.service.mapper;

import com.myself.cabinet.domain.Appointement;
import com.myself.cabinet.domain.Medecin;
import com.myself.cabinet.domain.Patient;
import com.myself.cabinet.domain.ShiftHoraire;
import com.myself.cabinet.service.dto.AppointementDTO;
import com.myself.cabinet.service.dto.MedecinDTO;
import com.myself.cabinet.service.dto.PatientDTO;
import com.myself.cabinet.service.dto.ShiftHoraireDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Appointement} and its DTO {@link AppointementDTO}.
 */
@Mapper(componentModel = "spring")
public interface AppointementMapper extends EntityMapper<AppointementDTO, Appointement> {
    @Mapping(target = "medecin", source = "medecin", qualifiedByName = "medecinId")
    @Mapping(target = "patient", source = "patient", qualifiedByName = "patientId")
    @Mapping(target = "shiftHoraire", source = "shiftHoraire", qualifiedByName = "shiftHoraireId")
    AppointementDTO toDto(Appointement s);

    @Named("medecinId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MedecinDTO toDtoMedecinId(Medecin medecin);

    @Named("patientId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PatientDTO toDtoPatientId(Patient patient);

    @Named("shiftHoraireId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ShiftHoraireDTO toDtoShiftHoraireId(ShiftHoraire shiftHoraire);
}
