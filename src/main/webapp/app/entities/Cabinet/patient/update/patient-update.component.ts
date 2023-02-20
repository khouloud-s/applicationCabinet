import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPatient, Patient } from '../patient.model';
import { PatientService } from '../service/patient.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    userUuid: [null, [Validators.required]],
    id: [],
    fullName: [null, [Validators.min(5), Validators.max(30)]],
    email: [],
    scanOrdonnance: [],
    scanOrdonnanceContentType: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('cabinetApp.error', { message: err.message })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
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

  protected updateForm(patient: IPatient): void {
    this.editForm.patchValue({
      userUuid: patient.userUuid,
      id: patient.id,
      fullName: patient.fullName,
      email: patient.email,
      scanOrdonnance: patient.scanOrdonnance,
      scanOrdonnanceContentType: patient.scanOrdonnanceContentType,
    });
  }

  protected createFromForm(): IPatient {
    return {
      ...new Patient(),
      userUuid: this.editForm.get(['userUuid'])!.value,
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      email: this.editForm.get(['email'])!.value,
      scanOrdonnanceContentType: this.editForm.get(['scanOrdonnanceContentType'])!.value,
      scanOrdonnance: this.editForm.get(['scanOrdonnance'])!.value,
    };
  }
}
