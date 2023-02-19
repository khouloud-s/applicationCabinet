import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IAppointement, Appointement } from '../appointement.model';
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

  medecinsSharedCollection: IMedecin[] = [];
  patientsSharedCollection: IPatient[] = [];
  shiftHorairesSharedCollection: IShiftHoraire[] = [];

  editForm = this.fb.group({
    userUuid: [null, [Validators.required]],
    id: [],
    date: [],
    isActive: [],
    medecin: [],
    patient: [],
    shiftHoraire: [],
  });

  constructor(
    protected appointementService: AppointementService,
    protected medecinService: MedecinService,
    protected patientService: PatientService,
    protected shiftHoraireService: ShiftHoraireService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointement }) => {
      this.updateForm(appointement);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const appointement = this.createFromForm();
    if (appointement.id !== undefined) {
      this.subscribeToSaveResponse(this.appointementService.update(appointement));
    } else {
      this.subscribeToSaveResponse(this.appointementService.create(appointement));
    }
  }

  trackMedecinById(_index: number, item: IMedecin): number {
    return item.id!;
  }

  trackPatientById(_index: number, item: IPatient): number {
    return item.id!;
  }

  trackShiftHoraireById(_index: number, item: IShiftHoraire): number {
    return item.id!;
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
    this.editForm.patchValue({
      userUuid: appointement.userUuid,
      id: appointement.id,
      date: appointement.date,
      isActive: appointement.isActive,
      medecin: appointement.medecin,
      patient: appointement.patient,
      shiftHoraire: appointement.shiftHoraire,
    });

    this.medecinsSharedCollection = this.medecinService.addMedecinToCollectionIfMissing(
      this.medecinsSharedCollection,
      appointement.medecin
    );
    this.patientsSharedCollection = this.patientService.addPatientToCollectionIfMissing(
      this.patientsSharedCollection,
      appointement.patient
    );
    this.shiftHorairesSharedCollection = this.shiftHoraireService.addShiftHoraireToCollectionIfMissing(
      this.shiftHorairesSharedCollection,
      appointement.shiftHoraire
    );
  }

  protected loadRelationshipsOptions(): void {
    this.medecinService
      .query()
      .pipe(map((res: HttpResponse<IMedecin[]>) => res.body ?? []))
      .pipe(
        map((medecins: IMedecin[]) => this.medecinService.addMedecinToCollectionIfMissing(medecins, this.editForm.get('medecin')!.value))
      )
      .subscribe((medecins: IMedecin[]) => (this.medecinsSharedCollection = medecins));

    this.patientService
      .query()
      .pipe(map((res: HttpResponse<IPatient[]>) => res.body ?? []))
      .pipe(
        map((patients: IPatient[]) => this.patientService.addPatientToCollectionIfMissing(patients, this.editForm.get('patient')!.value))
      )
      .subscribe((patients: IPatient[]) => (this.patientsSharedCollection = patients));

    this.shiftHoraireService
      .query()
      .pipe(map((res: HttpResponse<IShiftHoraire[]>) => res.body ?? []))
      .pipe(
        map((shiftHoraires: IShiftHoraire[]) =>
          this.shiftHoraireService.addShiftHoraireToCollectionIfMissing(shiftHoraires, this.editForm.get('shiftHoraire')!.value)
        )
      )
      .subscribe((shiftHoraires: IShiftHoraire[]) => (this.shiftHorairesSharedCollection = shiftHoraires));
  }

  protected createFromForm(): IAppointement {
    return {
      ...new Appointement(),
      userUuid: this.editForm.get(['userUuid'])!.value,
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
      medecin: this.editForm.get(['medecin'])!.value,
      patient: this.editForm.get(['patient'])!.value,
      shiftHoraire: this.editForm.get(['shiftHoraire'])!.value,
    };
  }
}
