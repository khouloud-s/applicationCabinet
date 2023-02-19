import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MyPatientRoutingModule } from './route/my-patients-routing.module';


@NgModule({
  imports: [SharedModule,MyPatientRoutingModule],
  declarations: [],
  entryComponents: [],
})
export class MyPatientModule {}
