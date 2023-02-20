import dayjs from 'dayjs/esm';
import { IMedecin } from 'app/entities/Cabinet/medecin/medecin.model';
import { IPatient } from 'app/entities/Cabinet/patient/patient.model';
import { IShiftHoraire } from 'app/entities/Cabinet/shift-horaire/shift-horaire.model';

export interface IAppointement {
  userUuid?: string | null;
  id: number;
  date?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  medecin?: Pick<IMedecin, 'id'> | null;
  patient?: Pick<IPatient, 'id'> | null;
  shiftHoraire?: Pick<IShiftHoraire, 'id'> | null;
}

export type NewAppointement = Omit<IAppointement, 'id'> & { id: null };
