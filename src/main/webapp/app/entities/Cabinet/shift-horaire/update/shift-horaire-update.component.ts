import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IShiftHoraire, ShiftHoraire } from '../shift-horaire.model';
import { ShiftHoraireService } from '../service/shift-horaire.service';

@Component({
  selector: 'jhi-shift-horaire-update',
  templateUrl: './shift-horaire-update.component.html',
})
export class ShiftHoraireUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    userUuid: [null, [Validators.required]],
    id: [],
    timeStart: [],
    timeEnd: [],
  });

  constructor(protected shiftHoraireService: ShiftHoraireService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shiftHoraire }) => {
      if (shiftHoraire.id === undefined) {
        const today = dayjs().startOf('day');
        shiftHoraire.timeStart = today;
        shiftHoraire.timeEnd = today;
      }

      this.updateForm(shiftHoraire);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shiftHoraire = this.createFromForm();
    if (shiftHoraire.id !== undefined) {
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
    this.editForm.patchValue({
      userUuid: shiftHoraire.userUuid,
      id: shiftHoraire.id,
      timeStart: shiftHoraire.timeStart ? shiftHoraire.timeStart.format(DATE_TIME_FORMAT) : null,
      timeEnd: shiftHoraire.timeEnd ? shiftHoraire.timeEnd.format(DATE_TIME_FORMAT) : null,
    });
  }

  protected createFromForm(): IShiftHoraire {
    return {
      ...new ShiftHoraire(),
      userUuid: this.editForm.get(['userUuid'])!.value,
      id: this.editForm.get(['id'])!.value,
      timeStart: this.editForm.get(['timeStart'])!.value ? dayjs(this.editForm.get(['timeStart'])!.value, DATE_TIME_FORMAT) : undefined,
      timeEnd: this.editForm.get(['timeEnd'])!.value ? dayjs(this.editForm.get(['timeEnd'])!.value, DATE_TIME_FORMAT) : undefined,
    };
  }
}
