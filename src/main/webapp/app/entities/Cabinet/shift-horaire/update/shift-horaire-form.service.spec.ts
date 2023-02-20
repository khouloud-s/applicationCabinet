import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../shift-horaire.test-samples';

import { ShiftHoraireFormService } from './shift-horaire-form.service';

describe('ShiftHoraire Form Service', () => {
  let service: ShiftHoraireFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ShiftHoraireFormService);
  });

  describe('Service methods', () => {
    describe('createShiftHoraireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createShiftHoraireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            userUuid: expect.any(Object),
            id: expect.any(Object),
            timeStart: expect.any(Object),
            timeEnd: expect.any(Object),
          })
        );
      });

      it('passing IShiftHoraire should create a new form with FormGroup', () => {
        const formGroup = service.createShiftHoraireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            userUuid: expect.any(Object),
            id: expect.any(Object),
            timeStart: expect.any(Object),
            timeEnd: expect.any(Object),
          })
        );
      });
    });

    describe('getShiftHoraire', () => {
      it('should return NewShiftHoraire for default ShiftHoraire initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createShiftHoraireFormGroup(sampleWithNewData);

        const shiftHoraire = service.getShiftHoraire(formGroup) as any;

        expect(shiftHoraire).toMatchObject(sampleWithNewData);
      });

      it('should return NewShiftHoraire for empty ShiftHoraire initial value', () => {
        const formGroup = service.createShiftHoraireFormGroup();

        const shiftHoraire = service.getShiftHoraire(formGroup) as any;

        expect(shiftHoraire).toMatchObject({});
      });

      it('should return IShiftHoraire', () => {
        const formGroup = service.createShiftHoraireFormGroup(sampleWithRequiredData);

        const shiftHoraire = service.getShiftHoraire(formGroup) as any;

        expect(shiftHoraire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IShiftHoraire should not enable id FormControl', () => {
        const formGroup = service.createShiftHoraireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewShiftHoraire should disable id FormControl', () => {
        const formGroup = service.createShiftHoraireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
