import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IRole, NewRole } from '../role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRole for edit and NewRoleFormGroupInput for create.
 */
type RoleFormGroupInput = IRole | PartialWithRequiredKeyOf<NewRole>;

type RoleFormDefaults = Pick<NewRole, 'id'>;

type RoleFormGroupContent = {
  userUuid: FormControl<IRole['userUuid']>;
  id: FormControl<IRole['id'] | NewRole['id']>;
  rolename: FormControl<IRole['rolename']>;
};

export type RoleFormGroup = FormGroup<RoleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoleFormService {
  createRoleFormGroup(role: RoleFormGroupInput = { id: null }): RoleFormGroup {
    const roleRawValue = {
      ...this.getFormDefaults(),
      ...role,
    };
    return new FormGroup<RoleFormGroupContent>({
      userUuid: new FormControl(roleRawValue.userUuid, {
        validators: [Validators.required],
      }),
      id: new FormControl(
        { value: roleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      rolename: new FormControl(roleRawValue.rolename),
    });
  }

  getRole(form: RoleFormGroup): IRole | NewRole {
    return form.getRawValue() as IRole | NewRole;
  }

  resetForm(form: RoleFormGroup, role: RoleFormGroupInput): void {
    const roleRawValue = { ...this.getFormDefaults(), ...role };
    form.reset(
      {
        ...roleRawValue,
        id: { value: roleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RoleFormDefaults {
    return {
      id: null,
    };
  }
}
