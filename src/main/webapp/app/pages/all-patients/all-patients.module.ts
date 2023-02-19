import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import {AllPatientRoutingModule} from "./route/all-patients-routing.module";


@NgModule({
  imports: [SharedModule,AllPatientRoutingModule],
  declarations: [],
  entryComponents: [],
})
export class AllPatientModule {}
