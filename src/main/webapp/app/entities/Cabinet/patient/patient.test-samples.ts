import { IPatient, NewPatient } from './patient.model';

export const sampleWithRequiredData: IPatient = {
  userUuid: '62d49289-3a7f-41f9-930c-0ff6d980fc38',
  id: 67802,
};

export const sampleWithPartialData: IPatient = {
  userUuid: 'a8962f63-5d09-41d6-8b6f-a3ce8ddec9d6',
  id: 55097,
  scanOrdonnance: '../fake-data/blob/hipster.png',
  scanOrdonnanceContentType: 'unknown',
};

export const sampleWithFullData: IPatient = {
  userUuid: 'fdaeb3f7-2565-46e9-b6b4-d0e330df4bd2',
  id: 13838,
  fullName: 'Avon',
  email: 'Gabriel19@yahoo.fr',
  scanOrdonnance: '../fake-data/blob/hipster.png',
  scanOrdonnanceContentType: 'unknown',
};

export const sampleWithNewData: NewPatient = {
  userUuid: '1697e883-f0d7-4c1b-9a9d-ad653bb24823',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
