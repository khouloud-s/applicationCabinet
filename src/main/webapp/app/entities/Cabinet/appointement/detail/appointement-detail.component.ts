import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAppointement } from '../appointement.model';

@Component({
  selector: 'jhi-appointement-detail',
  templateUrl: './appointement-detail.component.html',
})
export class AppointementDetailComponent implements OnInit {
  appointement: IAppointement | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ appointement }) => {
      this.appointement = appointement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
