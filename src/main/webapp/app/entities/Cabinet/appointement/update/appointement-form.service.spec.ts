import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../appointement.test-samples';

import { AppointementFormService } from './appointement-form.service';

describe('Appointement Form Service', () => {
  let service: AppointementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AppointementFormService);
  });

  describe('Service methods', () => {
    describe('createAppointementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAppointementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            userUuid: expect.any(Object),
            id: expect.any(Object),
            date: expect.any(Object),
            isActive: expect.any(Object),
            medecin: expect.any(Object),
            patient: expect.any(Object),
            shiftHoraire: expect.any(Object),
          })
        );
      });

      it('passing IAppointement should create a new form with FormGroup', () => {
        const formGroup = service.createAppointementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            userUuid: expect.any(Object),
            id: expect.any(Object),
            date: expect.any(Object),
            isActive: expect.any(Object),
            medecin: expect.any(Object),
            patient: expect.any(Object),
            shiftHoraire: expect.any(Object),
          })
        );
      });
    });

    describe('getAppointement', () => {
      it('should return NewAppointement for default Appointement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createAppointementFormGroup(sampleWithNewData);

        const appointement = service.getAppointement(formGroup) as any;

        expect(appointement).toMatchObject(sampleWithNewData);
      });

      it('should return NewAppointement for empty Appointement initial value', () => {
        const formGroup = service.createAppointementFormGroup();

        const appointement = service.getAppointement(formGroup) as any;

        expect(appointement).toMatchObject({});
      });

      it('should return IAppointement', () => {
        const formGroup = service.createAppointementFormGroup(sampleWithRequiredData);

        const appointement = service.getAppointement(formGroup) as any;

        expect(appointement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAppointement should not enable id FormControl', () => {
        const formGroup = service.createAppointementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAppointement should disable id FormControl', () => {
        const formGroup = service.createAppointementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
