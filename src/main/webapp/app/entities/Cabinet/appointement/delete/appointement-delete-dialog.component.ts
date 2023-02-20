import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAppointement } from '../appointement.model';
import { AppointementService } from '../service/appointement.service';

@Component({
  templateUrl: './appointement-delete-dialog.component.html',
})
export class AppointementDeleteDialogComponent {
  appointement?: IAppointement;

  constructor(protected appointementService: AppointementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.appointementService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
