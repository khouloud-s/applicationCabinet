import { RoleName } from 'app/entities/enumerations/role-name.model';

export interface IRole {
  userUuid?: string | null;
  id: number;
  rolename?: RoleName | null;
}

export type NewRole = Omit<IRole, 'id'> & { id: null };
