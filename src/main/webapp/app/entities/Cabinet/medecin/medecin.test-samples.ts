import { IMedecin, NewMedecin } from './medecin.model';

export const sampleWithRequiredData: IMedecin = {
  userUuid: '64b48989-4282-411a-9bd7-b2016cb6c5a3',
  id: 92571,
};

export const sampleWithPartialData: IMedecin = {
  userUuid: '22c957cf-04b4-41e4-bd92-aa48f4407d98',
  id: 66953,
  fullName: 'de Chips',
  phone: '0499438271',
  isActive: true,
};

export const sampleWithFullData: IMedecin = {
  userUuid: 'fbf6d1c5-9a28-4721-a42d-164de68ed648',
  id: 80967,
  fullName: 'Mouse',
  phone: '+33 544634764',
  adress: 'Market redefine',
  isActive: true,
};

export const sampleWithNewData: NewMedecin = {
  userUuid: '1ea752e5-6112-49c9-b0c7-5a97299e1f00',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
