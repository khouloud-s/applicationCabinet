import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ShiftHoraireComponent } from './list/shift-horaire.component';
import { ShiftHoraireDetailComponent } from './detail/shift-horaire-detail.component';
import { ShiftHoraireUpdateComponent } from './update/shift-horaire-update.component';
import { ShiftHoraireDeleteDialogComponent } from './delete/shift-horaire-delete-dialog.component';
import { ShiftHoraireRoutingModule } from './route/shift-horaire-routing.module';

@NgModule({
  imports: [SharedModule, ShiftHoraireRoutingModule],
  declarations: [ShiftHoraireComponent, ShiftHoraireDetailComponent, ShiftHoraireUpdateComponent, ShiftHoraireDeleteDialogComponent],
  entryComponents: [ShiftHoraireDeleteDialogComponent],
})
export class CabinetShiftHoraireModule {}
