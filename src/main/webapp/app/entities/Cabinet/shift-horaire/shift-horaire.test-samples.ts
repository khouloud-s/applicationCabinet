import dayjs from 'dayjs/esm';

import { IShiftHoraire, NewShiftHoraire } from './shift-horaire.model';

export const sampleWithRequiredData: IShiftHoraire = {
  userUuid: '810f6b7e-18b1-46cb-bc53-f6bea1891ee8',
  id: 85723,
};

export const sampleWithPartialData: IShiftHoraire = {
  userUuid: '92875560-35d9-4d17-b8b7-8892bbb972aa',
  id: 85357,
};

export const sampleWithFullData: IShiftHoraire = {
  userUuid: '626b1bbe-c56e-4edd-af9a-251bf3049eac',
  id: 32932,
  timeStart: dayjs('2022-05-20T02:19'),
  timeEnd: dayjs('2022-05-19T12:03'),
};

export const sampleWithNewData: NewShiftHoraire = {
  userUuid: 'd492c5c8-f5f9-4942-be44-5ddd8450cdc0',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
