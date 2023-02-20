import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AppointementComponent } from '../list/appointement.component';
import { AppointementDetailComponent } from '../detail/appointement-detail.component';
import { AppointementUpdateComponent } from '../update/appointement-update.component';
import { AppointementRoutingResolveService } from './appointement-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const appointementRoute: Routes = [
  {
    path: '',
    component: AppointementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AppointementDetailComponent,
    resolve: {
      appointement: AppointementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AppointementUpdateComponent,
    resolve: {
      appointement: AppointementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AppointementUpdateComponent,
    resolve: {
      appointement: AppointementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(appointementRoute)],
  exports: [RouterModule],
})
export class AppointementRoutingModule {}
