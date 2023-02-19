import { RoleName } from 'app/entities/enumerations/role-name.model';

export interface IRole {
  userUuid?: string;
  id?: number;
  rolename?: RoleName | null;
}

export class Role implements IRole {
  constructor(public userUuid?: string, public id?: number, public rolename?: RoleName | null) {}
}

export function getRoleIdentifier(role: IRole): number | undefined {
  return role.id;
}
