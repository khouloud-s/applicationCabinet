import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AppointementComponent } from './list/appointement.component';
import { AppointementDetailComponent } from './detail/appointement-detail.component';
import { AppointementUpdateComponent } from './update/appointement-update.component';
import { AppointementDeleteDialogComponent } from './delete/appointement-delete-dialog.component';
import { AppointementRoutingModule } from './route/appointement-routing.module';

@NgModule({
  imports: [SharedModule, AppointementRoutingModule],
  declarations: [AppointementComponent, AppointementDetailComponent, AppointementUpdateComponent, AppointementDeleteDialogComponent],
  entryComponents: [AppointementDeleteDialogComponent],
})
export class CabinetAppointementModule {}
