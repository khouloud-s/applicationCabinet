import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DashboardComponent } from '../dashboard.component';


const DashboardRoute: Routes = [
  {
    path: '',
    component: DashboardComponent,
    data: {
      defaultSort: 'id,asc',
      resolve: {
      },
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(DashboardRoute)],
  exports: [RouterModule],
})
export class DashboardRoutingModule { }
