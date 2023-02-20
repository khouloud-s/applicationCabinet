import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPatient, NewPatient } from '../patient.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPatient for edit and NewPatientFormGroupInput for create.
 */
type PatientFormGroupInput = IPatient | PartialWithRequiredKeyOf<NewPatient>;

type PatientFormDefaults = Pick<NewPatient, 'id'>;

type PatientFormGroupContent = {
  userUuid: FormControl<IPatient['userUuid']>;
  id: FormControl<IPatient['id'] | NewPatient['id']>;
  fullName: FormControl<IPatient['fullName']>;
  email: FormControl<IPatient['email']>;
  scanOrdonnance: FormControl<IPatient['scanOrdonnance']>;
  scanOrdonnanceContentType: FormControl<IPatient['scanOrdonnanceContentType']>;
};

export type PatientFormGroup = FormGroup<PatientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PatientFormService {
  createPatientFormGroup(patient: PatientFormGroupInput = { id: null }): PatientFormGroup {
    const patientRawValue = {
      ...this.getFormDefaults(),
      ...patient,
    };
    return new FormGroup<PatientFormGroupContent>({
      userUuid: new FormControl(patientRawValue.userUuid, {
        validators: [Validators.required],
      }),
      id: new FormControl(
        { value: patientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      fullName: new FormControl(patientRawValue.fullName, {
        validators: [Validators.min(5), Validators.max(30)],
      }),
      email: new FormControl(patientRawValue.email),
      scanOrdonnance: new FormControl(patientRawValue.scanOrdonnance),
      scanOrdonnanceContentType: new FormControl(patientRawValue.scanOrdonnanceContentType),
    });
  }

  getPatient(form: PatientFormGroup): IPatient | NewPatient {
    return form.getRawValue() as IPatient | NewPatient;
  }

  resetForm(form: PatientFormGroup, patient: PatientFormGroupInput): void {
    const patientRawValue = { ...this.getFormDefaults(), ...patient };
    form.reset(
      {
        ...patientRawValue,
        id: { value: patientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PatientFormDefaults {
    return {
      id: null,
    };
  }
}
