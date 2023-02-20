import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAppointement, NewAppointement } from '../appointement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAppointement for edit and NewAppointementFormGroupInput for create.
 */
type AppointementFormGroupInput = IAppointement | PartialWithRequiredKeyOf<NewAppointement>;

type AppointementFormDefaults = Pick<NewAppointement, 'id' | 'isActive'>;

type AppointementFormGroupContent = {
  userUuid: FormControl<IAppointement['userUuid']>;
  id: FormControl<IAppointement['id'] | NewAppointement['id']>;
  date: FormControl<IAppointement['date']>;
  isActive: FormControl<IAppointement['isActive']>;
  medecin: FormControl<IAppointement['medecin']>;
  patient: FormControl<IAppointement['patient']>;
  shiftHoraire: FormControl<IAppointement['shiftHoraire']>;
};

export type AppointementFormGroup = FormGroup<AppointementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AppointementFormService {
  createAppointementFormGroup(appointement: AppointementFormGroupInput = { id: null }): AppointementFormGroup {
    const appointementRawValue = {
      ...this.getFormDefaults(),
      ...appointement,
    };
    return new FormGroup<AppointementFormGroupContent>({
      userUuid: new FormControl(appointementRawValue.userUuid, {
        validators: [Validators.required],
      }),
      id: new FormControl(
        { value: appointementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(appointementRawValue.date),
      isActive: new FormControl(appointementRawValue.isActive),
      medecin: new FormControl(appointementRawValue.medecin),
      patient: new FormControl(appointementRawValue.patient),
      shiftHoraire: new FormControl(appointementRawValue.shiftHoraire),
    });
  }

  getAppointement(form: AppointementFormGroup): IAppointement | NewAppointement {
    return form.getRawValue() as IAppointement | NewAppointement;
  }

  resetForm(form: AppointementFormGroup, appointement: AppointementFormGroupInput): void {
    const appointementRawValue = { ...this.getFormDefaults(), ...appointement };
    form.reset(
      {
        ...appointementRawValue,
        id: { value: appointementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AppointementFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
