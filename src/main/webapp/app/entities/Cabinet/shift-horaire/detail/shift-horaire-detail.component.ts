import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShiftHoraire } from '../shift-horaire.model';

@Component({
  selector: 'jhi-shift-horaire-detail',
  templateUrl: './shift-horaire-detail.component.html',
})
export class ShiftHoraireDetailComponent implements OnInit {
  shiftHoraire: IShiftHoraire | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shiftHoraire }) => {
      this.shiftHoraire = shiftHoraire;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
