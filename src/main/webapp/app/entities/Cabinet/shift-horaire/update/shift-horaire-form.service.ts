import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IShiftHoraire, NewShiftHoraire } from '../shift-horaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IShiftHoraire for edit and NewShiftHoraireFormGroupInput for create.
 */
type ShiftHoraireFormGroupInput = IShiftHoraire | PartialWithRequiredKeyOf<NewShiftHoraire>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IShiftHoraire | NewShiftHoraire> = Omit<T, 'timeStart' | 'timeEnd'> & {
  timeStart?: string | null;
  timeEnd?: string | null;
};

type ShiftHoraireFormRawValue = FormValueOf<IShiftHoraire>;

type NewShiftHoraireFormRawValue = FormValueOf<NewShiftHoraire>;

type ShiftHoraireFormDefaults = Pick<NewShiftHoraire, 'id' | 'timeStart' | 'timeEnd'>;

type ShiftHoraireFormGroupContent = {
  userUuid: FormControl<ShiftHoraireFormRawValue['userUuid']>;
  id: FormControl<ShiftHoraireFormRawValue['id'] | NewShiftHoraire['id']>;
  timeStart: FormControl<ShiftHoraireFormRawValue['timeStart']>;
  timeEnd: FormControl<ShiftHoraireFormRawValue['timeEnd']>;
};

export type ShiftHoraireFormGroup = FormGroup<ShiftHoraireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ShiftHoraireFormService {
  createShiftHoraireFormGroup(shiftHoraire: ShiftHoraireFormGroupInput = { id: null }): ShiftHoraireFormGroup {
    const shiftHoraireRawValue = this.convertShiftHoraireToShiftHoraireRawValue({
      ...this.getFormDefaults(),
      ...shiftHoraire,
    });
    return new FormGroup<ShiftHoraireFormGroupContent>({
      userUuid: new FormControl(shiftHoraireRawValue.userUuid, {
        validators: [Validators.required],
      }),
      id: new FormControl(
        { value: shiftHoraireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      timeStart: new FormControl(shiftHoraireRawValue.timeStart),
      timeEnd: new FormControl(shiftHoraireRawValue.timeEnd),
    });
  }

  getShiftHoraire(form: ShiftHoraireFormGroup): IShiftHoraire | NewShiftHoraire {
    return this.convertShiftHoraireRawValueToShiftHoraire(form.getRawValue() as ShiftHoraireFormRawValue | NewShiftHoraireFormRawValue);
  }

  resetForm(form: ShiftHoraireFormGroup, shiftHoraire: ShiftHoraireFormGroupInput): void {
    const shiftHoraireRawValue = this.convertShiftHoraireToShiftHoraireRawValue({ ...this.getFormDefaults(), ...shiftHoraire });
    form.reset(
      {
        ...shiftHoraireRawValue,
        id: { value: shiftHoraireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ShiftHoraireFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      timeStart: currentTime,
      timeEnd: currentTime,
    };
  }

  private convertShiftHoraireRawValueToShiftHoraire(
    rawShiftHoraire: ShiftHoraireFormRawValue | NewShiftHoraireFormRawValue
  ): IShiftHoraire | NewShiftHoraire {
    return {
      ...rawShiftHoraire,
      timeStart: dayjs(rawShiftHoraire.timeStart, DATE_TIME_FORMAT),
      timeEnd: dayjs(rawShiftHoraire.timeEnd, DATE_TIME_FORMAT),
    };
  }

  private convertShiftHoraireToShiftHoraireRawValue(
    shiftHoraire: IShiftHoraire | (Partial<NewShiftHoraire> & ShiftHoraireFormDefaults)
  ): ShiftHoraireFormRawValue | PartialWithRequiredKeyOf<NewShiftHoraireFormRawValue> {
    return {
      ...shiftHoraire,
      timeStart: shiftHoraire.timeStart ? shiftHoraire.timeStart.format(DATE_TIME_FORMAT) : undefined,
      timeEnd: shiftHoraire.timeEnd ? shiftHoraire.timeEnd.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
