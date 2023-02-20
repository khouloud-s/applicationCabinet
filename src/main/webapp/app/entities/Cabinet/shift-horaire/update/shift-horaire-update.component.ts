import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ShiftHoraireFormService, ShiftHoraireFormGroup } from './shift-horaire-form.service';
import { IShiftHoraire } from '../shift-horaire.model';
import { ShiftHoraireService } from '../service/shift-horaire.service';

@Component({
  selector: 'jhi-shift-horaire-update',
  templateUrl: './shift-horaire-update.component.html',
})
export class ShiftHoraireUpdateComponent implements OnInit {
  isSaving = false;
  shiftHoraire: IShiftHoraire | null = null;

  editForm: ShiftHoraireFormGroup = this.shiftHoraireFormService.createShiftHoraireFormGroup();

  constructor(
    protected shiftHoraireService: ShiftHoraireService,
    protected shiftHoraireFormService: ShiftHoraireFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shiftHoraire }) => {
      this.shiftHoraire = shiftHoraire;
      if (shiftHoraire) {
        this.updateForm(shiftHoraire);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shiftHoraire = this.shiftHoraireFormService.getShiftHoraire(this.editForm);
    if (shiftHoraire.id !== null) {
      this.subscribeToSaveResponse(this.shiftHoraireService.update(shiftHoraire));
    } else {
      this.subscribeToSaveResponse(this.shiftHoraireService.create(shiftHoraire));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShiftHoraire>>): void {
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

  protected updateForm(shiftHoraire: IShiftHoraire): void {
    this.shiftHoraire = shiftHoraire;
    this.shiftHoraireFormService.resetForm(this.editForm, shiftHoraire);
  }
}
