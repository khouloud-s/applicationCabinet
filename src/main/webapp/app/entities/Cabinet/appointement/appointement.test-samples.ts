import dayjs from 'dayjs/esm';

import { IAppointement, NewAppointement } from './appointement.model';

export const sampleWithRequiredData: IAppointement = {
  userUuid: '1644ec19-0442-47dd-a8d0-abbca1cf0a48',
  id: 59150,
};

export const sampleWithPartialData: IAppointement = {
  userUuid: 'dee86cea-5bd3-4a59-98a3-59f04e1c6a08',
  id: 73102,
  date: dayjs('2022-05-19'),
  isActive: false,
};

export const sampleWithFullData: IAppointement = {
  userUuid: '54649bb4-d856-4054-9f29-51f939273bac',
  id: 23241,
  date: dayjs('2022-05-19'),
  isActive: true,
};

export const sampleWithNewData: NewAppointement = {
  userUuid: 'f92c6108-9671-4bcb-a1c1-68a2042936a1',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
