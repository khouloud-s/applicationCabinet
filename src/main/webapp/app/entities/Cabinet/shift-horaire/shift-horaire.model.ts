import dayjs from 'dayjs/esm';

export interface IShiftHoraire {
  userUuid?: string | null;
  id: number;
  timeStart?: dayjs.Dayjs | null;
  timeEnd?: dayjs.Dayjs | null;
}

export type NewShiftHoraire = Omit<IShiftHoraire, 'id'> & { id: null };
