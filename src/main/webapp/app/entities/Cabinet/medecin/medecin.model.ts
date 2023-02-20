export interface IMedecin {
  userUuid?: string | null;
  id: number;
  fullName?: string | null;
  phone?: string | null;
  adress?: string | null;
  isActive?: boolean | null;
}

export type NewMedecin = Omit<IMedecin, 'id'> & { id: null };
