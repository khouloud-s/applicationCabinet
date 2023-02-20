import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { AppointementFormService } from './appointement-form.service';
import { AppointementService } from '../service/appointement.service';
import { IAppointement } from '../appointement.model';
import { IMedecin } from 'app/entities/Cabinet/medecin/medecin.model';
import { MedecinService } from 'app/entities/Cabinet/medecin/service/medecin.service';
import { IPatient } from 'app/entities/Cabinet/patient/patient.model';
import { PatientService } from 'app/entities/Cabinet/patient/service/patient.service';
import { IShiftHoraire } from 'app/entities/Cabinet/shift-horaire/shift-horaire.model';
import { ShiftHoraireService } from 'app/entities/Cabinet/shift-horaire/service/shift-horaire.service';

import { AppointementUpdateComponent } from './appointement-update.component';

describe('Appointement Management Update Component', () => {
  let comp: AppointementUpdateComponent;
  let fixture: ComponentFixture<AppointementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let appointementFormService: AppointementFormService;
  let appointementService: AppointementService;
  let medecinService: MedecinService;
  let patientService: PatientService;
  let shiftHoraireService: ShiftHoraireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [AppointementUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AppointementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AppointementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    appointementFormService = TestBed.inject(AppointementFormService);
    appointementService = TestBed.inject(AppointementService);
    medecinService = TestBed.inject(MedecinService);
    patientService = TestBed.inject(PatientService);
    shiftHoraireService = TestBed.inject(ShiftHoraireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Medecin query and add missing value', () => {
      const appointement: IAppointement = { id: 456 };
      const medecin: IMedecin = { id: 9513 };
      appointement.medecin = medecin;

      const medecinCollection: IMedecin[] = [{ id: 23592 }];
      jest.spyOn(medecinService, 'query').mockReturnValue(of(new HttpResponse({ body: medecinCollection })));
      const additionalMedecins = [medecin];
      const expectedCollection: IMedecin[] = [...additionalMedecins, ...medecinCollection];
      jest.spyOn(medecinService, 'addMedecinToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointement });
      comp.ngOnInit();

      expect(medecinService.query).toHaveBeenCalled();
      expect(medecinService.addMedecinToCollectionIfMissing).toHaveBeenCalledWith(
        medecinCollection,
        ...additionalMedecins.map(expect.objectContaining)
      );
      expect(comp.medecinsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Patient query and add missing value', () => {
      const appointement: IAppointement = { id: 456 };
      const patient: IPatient = { id: 2485 };
      appointement.patient = patient;

      const patientCollection: IPatient[] = [{ id: 80614 }];
      jest.spyOn(patientService, 'query').mockReturnValue(of(new HttpResponse({ body: patientCollection })));
      const additionalPatients = [patient];
      const expectedCollection: IPatient[] = [...additionalPatients, ...patientCollection];
      jest.spyOn(patientService, 'addPatientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointement });
      comp.ngOnInit();

      expect(patientService.query).toHaveBeenCalled();
      expect(patientService.addPatientToCollectionIfMissing).toHaveBeenCalledWith(
        patientCollection,
        ...additionalPatients.map(expect.objectContaining)
      );
      expect(comp.patientsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call ShiftHoraire query and add missing value', () => {
      const appointement: IAppointement = { id: 456 };
      const shiftHoraire: IShiftHoraire = { id: 55238 };
      appointement.shiftHoraire = shiftHoraire;

      const shiftHoraireCollection: IShiftHoraire[] = [{ id: 65471 }];
      jest.spyOn(shiftHoraireService, 'query').mockReturnValue(of(new HttpResponse({ body: shiftHoraireCollection })));
      const additionalShiftHoraires = [shiftHoraire];
      const expectedCollection: IShiftHoraire[] = [...additionalShiftHoraires, ...shiftHoraireCollection];
      jest.spyOn(shiftHoraireService, 'addShiftHoraireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ appointement });
      comp.ngOnInit();

      expect(shiftHoraireService.query).toHaveBeenCalled();
      expect(shiftHoraireService.addShiftHoraireToCollectionIfMissing).toHaveBeenCalledWith(
        shiftHoraireCollection,
        ...additionalShiftHoraires.map(expect.objectContaining)
      );
      expect(comp.shiftHorairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const appointement: IAppointement = { id: 456 };
      const medecin: IMedecin = { id: 82202 };
      appointement.medecin = medecin;
      const patient: IPatient = { id: 34145 };
      appointement.patient = patient;
      const shiftHoraire: IShiftHoraire = { id: 75274 };
      appointement.shiftHoraire = shiftHoraire;

      activatedRoute.data = of({ appointement });
      comp.ngOnInit();

      expect(comp.medecinsSharedCollection).toContain(medecin);
      expect(comp.patientsSharedCollection).toContain(patient);
      expect(comp.shiftHorairesSharedCollection).toContain(shiftHoraire);
      expect(comp.appointement).toEqual(appointement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointement>>();
      const appointement = { id: 123 };
      jest.spyOn(appointementFormService, 'getAppointement').mockReturnValue(appointement);
      jest.spyOn(appointementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointement }));
      saveSubject.complete();

      // THEN
      expect(appointementFormService.getAppointement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(appointementService.update).toHaveBeenCalledWith(expect.objectContaining(appointement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointement>>();
      const appointement = { id: 123 };
      jest.spyOn(appointementFormService, 'getAppointement').mockReturnValue({ id: null });
      jest.spyOn(appointementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: appointement }));
      saveSubject.complete();

      // THEN
      expect(appointementFormService.getAppointement).toHaveBeenCalled();
      expect(appointementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAppointement>>();
      const appointement = { id: 123 };
      jest.spyOn(appointementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ appointement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(appointementService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareMedecin', () => {
      it('Should forward to medecinService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(medecinService, 'compareMedecin');
        comp.compareMedecin(entity, entity2);
        expect(medecinService.compareMedecin).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('comparePatient', () => {
      it('Should forward to patientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(patientService, 'comparePatient');
        comp.comparePatient(entity, entity2);
        expect(patientService.comparePatient).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareShiftHoraire', () => {
      it('Should forward to shiftHoraireService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(shiftHoraireService, 'compareShiftHoraire');
        comp.compareShiftHoraire(entity, entity2);
        expect(shiftHoraireService.compareShiftHoraire).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
