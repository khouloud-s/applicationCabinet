export interface IPatient {
  userUuid?: string | null;
  id: number;
  fullName?: string | null;
  email?: string | null;
  scanOrdonnance?: string | null;
  scanOrdonnanceContentType?: string | null;
}

export type NewPatient = Omit<IPatient, 'id'> & { id: null };
