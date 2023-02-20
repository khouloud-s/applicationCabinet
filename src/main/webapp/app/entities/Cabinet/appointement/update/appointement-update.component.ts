import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { AppointementFormService, AppointementFormGroup } from './appointement-form.service';
import { IAppointement } from '../appointement.model';
import { AppointementService } from '../service/appointement.service';
import { IMedecin } from 'app/entities/Cabinet/medecin/medecin.model';
import { MedecinService } from 'app/entities/Cabinet/medecin/service/medecin.service';
import { IPatient } from 'app/entities/Cabinet/patient/patient.model';
import { PatientService } from 'app/entities/Cabinet/patient/service/patient.service';
import { IShiftHoraire } from 'app/entities/Cabinet/shift-horaire/shift-horaire.model';
import { ShiftHoraireService } from 'app/entities/Cabinet/shift-horaire/service/shift-horaire.service';

@Component({
  selector: 'jhi-appointement-update',
  templateUrl: './appointement-update.component.html',
})
export class AppointementUpdateComponent implements OnInit {
  isSaving = false;
  appointement: IAppointement | null = null;

  medecinsSharedCollection: IMedecin[] = [];
  patientsSharedCollection: IPatient[] = [];
  shiftHorairesSharedCollection: IShiftHoraire[] = [];

  editForm: AppointementFormGroup = this.appointementFormService.createAppointementFormGroup();

  constructor(
    protected appointementService: AppointementService,
    protected appointementFormService: AppointementFormService,
    protected medecinService: MedecinService,
    protected patientService: PatientService,
    protected shiftHoraireService: ShiftHoraireService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareMedecin = (o1: IMedecin | null, o2: IMedecin | null): boolean => this.medecinService.compareMedecin(o1, o2);

  comparePatient = (o1: IPatient | null, o2: IPatient | null): boolean => this.patientService.comparePatient(o1, o2);

  compareShiftHoraire = (o1: IShiftHoraire | null, o2: IShiftHoraire | null): boolean =>
    this.shiftHoraireService.compareShiftHoraire(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointement }) => {
      this.appointement = appointement;
      if (appointement) {
        this.updateForm(appointement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointement = this.appointementFormService.getAppointement(this.editForm);
    if (appointement.id !== null) {
      this.subscribeToSaveResponse(this.appointementService.update(appointement));
    } else {
      this.subscribeToSaveResponse(this.appointementService.create(appointement));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAppointement>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(appointement: IAppointement): void {
    this.appointement = appointement;
    this.appointementFormService.resetForm(this.editForm, appointement);

    this.medecinsSharedCollection = this.medecinService.addMedecinToCollectionIfMissing<IMedecin>(
      this.medecinsSharedCollection,
      appointement.medecin
    );
    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing<IPatient>(
      this.patientsSharedCollection,
      appointement.patient
    );
    this.shiftHorairesSharedCollection = this.shiftHoraireService.addShiftHoraireToCollectionIfMissing<IShiftHoraire>(
      this.shiftHorairesSharedCollection,
      appointement.shiftHoraire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medecinService
      .query()
      .pipe(map((res: HttpResponse<IMedecin[]>) => res.body ?? []))
      .pipe(
        map((medecins: IMedecin[]) => this.medecinService.addMedecinToCollectionIfMissing<IMedecin>(medecins, this.appointement?.medecin))
      )
      .subscribe((medecins: IMedecin[]) => (this.medecinsSharedCollection = medecins));

    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing<IPatient>(patients, this.appointement?.patient))
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));

    this.shiftHoraireService
      .query()
      .pipe(map((res: HttpResponse<IShiftHoraire[]>) => res.body ?? []))
      .pipe(
        map((shiftHoraires: IShiftHoraire[]) =>
          this.shiftHoraireService.addShiftHoraireToCollectionIfMissing<IShiftHoraire>(shiftHoraires, this.appointement?.shiftHoraire)
        )
      )
      .subscribe((shiftHoraires: IShiftHoraire[]) => (this.shiftHorairesSharedCollection = shiftHoraires));
  }
}
