import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShiftHoraire } from '../shift-horaire.model';
import { ShiftHoraireService } from '../service/shift-horaire.service';

@Component({
  templateUrl: './shift-horaire-delete-dialog.component.html',
})
export class ShiftHoraireDeleteDialogComponent {
  shiftHoraire?: IShiftHoraire;

  constructor(protected shiftHoraireService: ShiftHoraireService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shiftHoraireService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
