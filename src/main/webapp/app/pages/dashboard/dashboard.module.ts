import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DashboardRoutingModule } from './route/dashboard-routing.module';


@NgModule({
  imports: [SharedModule,DashboardRoutingModule],
  declarations: [],
  entryComponents: [],
})
export class DashboardModule {}
