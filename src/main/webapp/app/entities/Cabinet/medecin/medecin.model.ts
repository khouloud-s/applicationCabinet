import { IAppointement } from 'app/entities/Cabinet/appointement/appointement.model';

export interface IMedecin {
  userUuid?: string;
  id?: number;
  fullName?: string | null;
  phone?: string | null;
  adress?: string | null;
  isActive?: boolean | null;
  appointements?: IAppointement[] | null;
}

export class Medecin implements IMedecin {
  constructor(
    public userUuid?: string,
    public id?: number,
    public fullName?: string | null,
    public phone?: string | null,
    public adress?: string | null,
    public isActive?: boolean | null,
    public appointements?: IAppointement[] | null
  ) {
    this.isActive = this.isActive ?? false;
  }
}

export function getMedecinIdentifier(medecin: IMedecin): number | undefined {
  return medecin.id;
}
