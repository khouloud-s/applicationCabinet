import dayjs from 'dayjs/esm';
import { IMedecin } from 'app/entities/Cabinet/medecin/medecin.model';
import { IPatient } from 'app/entities/Cabinet/patient/patient.model';
import { IShiftHoraire } from 'app/entities/Cabinet/shift-horaire/shift-horaire.model';

export interface IAppointement {
  userUuid?: string;
  id?: number;
  date?: dayjs.Dayjs | null;
  isActive?: boolean | null;
  medecin?: IMedecin | null;
  patient?: IPatient | null;
  shiftHoraire?: IShiftHoraire | null;
}

export class Appointement implements IAppointement {
  constructor(
    public userUuid?: string,
    public id?: number,
    public date?: dayjs.Dayjs | null,
    public isActive?: boolean | null,
    public medecin?: IMedecin | null,
    public patient?: IPatient | null,
    public shiftHoraire?: IShiftHoraire | null
  ) {
    this.isActive = this.isActive ?? false;
  }
}

export function getAppointementIdentifier(appointement: IAppointement): number | undefined {
  return appointement.id;
}
