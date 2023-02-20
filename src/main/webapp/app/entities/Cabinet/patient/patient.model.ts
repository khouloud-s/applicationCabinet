import { IAppointement } from 'app/entities/Cabinet/appointement/appointement.model';

export interface IPatient {
  userUuid?: string;
  id?: number;
  fullName?: string | null;
  email?: string | null;
  scanOrdonnanceContentType?: string | null;
  scanOrdonnance?: string | null;
  appointements?: IAppointement[] | null;
}

export class Patient implements IPatient {
  constructor(
    public userUuid?: string,
    public id?: number,
    public fullName?: string | null,
    public email?: string | null,
    public scanOrdonnanceContentType?: string | null,
    public scanOrdonnance?: string | null,
    public appointements?: IAppointement[] | null
  ) {}
}

export function getPatientIdentifier(patient: IPatient): number | undefined {
  return patient.id;
}
