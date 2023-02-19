import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IMedecin, Medecin } from '../medecin.model';
import { MedecinService } from '../service/medecin.service';

@Component({
  selector: 'jhi-medecin-update',
  templateUrl: './medecin-update.component.html',
})
export class MedecinUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    userUuid: [null, [Validators.required]],
    id: [],
    fullName: [],
    phone: [],
    adress: [],
    isActive: [],
  });

  constructor(protected medecinService: MedecinService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ medecin }) => {
      this.updateForm(medecin);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const medecin = this.createFromForm();
    if (medecin.id !== undefined) {
      this.subscribeToSaveResponse(this.medecinService.update(medecin));
    } else {
      this.subscribeToSaveResponse(this.medecinService.create(medecin));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedecin>>): void {
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

  protected updateForm(medecin: IMedecin): void {
    this.editForm.patchValue({
      userUuid: medecin.userUuid,
      id: medecin.id,
      fullName: medecin.fullName,
      phone: medecin.phone,
      adress: medecin.adress,
      isActive: medecin.isActive,
    });
  }

  protected createFromForm(): IMedecin {
    return {
      ...new Medecin(),
      userUuid: this.editForm.get(['userUuid'])!.value,
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      adress: this.editForm.get(['adress'])!.value,
      isActive: this.editForm.get(['isActive'])!.value,
    };
  }
}
