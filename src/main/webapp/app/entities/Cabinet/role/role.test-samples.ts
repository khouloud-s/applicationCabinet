import { RoleName } from 'app/entities/enumerations/role-name.model';

import { IRole, NewRole } from './role.model';

export const sampleWithRequiredData: IRole = {
  userUuid: '9f76f9d6-657b-4d73-b487-0442d0cc8bd7',
  id: 33296,
};

export const sampleWithPartialData: IRole = {
  userUuid: '9c22b1dd-e514-4edd-85a0-9f02753427a1',
  id: 10207,
};

export const sampleWithFullData: IRole = {
  userUuid: '7f5eabbf-8a05-4ab7-b8b2-9a3b308110f2',
  id: 92199,
  rolename: RoleName['ROLE_ADMIN'],
};

export const sampleWithNewData: NewRole = {
  userUuid: '4d39e36e-4567-4e76-a595-adeaaf9fc128',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
