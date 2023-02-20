import dayjs from 'dayjs/esm';
import { IAppointement } from 'app/entities/Cabinet/appointement/appointement.model';

export interface IShiftHoraire {
  userUuid?: string;
  id?: number;
  timeStart?: dayjs.Dayjs | null;
  timeEnd?: dayjs.Dayjs | null;
  appointements?: IAppointement[] | null;
}

export class ShiftHoraire implements IShiftHoraire {
  constructor(
    public userUuid?: string,
    public id?: number,
    public timeStart?: dayjs.Dayjs | null,
    public timeEnd?: dayjs.Dayjs | null,
    public appointements?: IAppointement[] | null
  ) {}
}

export function getShiftHoraireIdentifier(shiftHoraire: IShiftHoraire): number | undefined {
  return shiftHoraire.id;
}
