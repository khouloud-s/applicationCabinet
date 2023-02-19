import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShiftHoraireComponent } from '../list/shift-horaire.component';
import { ShiftHoraireDetailComponent } from '../detail/shift-horaire-detail.component';
import { ShiftHoraireUpdateComponent } from '../update/shift-horaire-update.component';
import { ShiftHoraireRoutingResolveService } from './shift-horaire-routing-resolve.service';

const shiftHoraireRoute: Routes = [
  {
    path: '',
    component: ShiftHoraireComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShiftHoraireDetailComponent,
    resolve: {
      shiftHoraire: ShiftHoraireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShiftHoraireUpdateComponent,
    resolve: {
      shiftHoraire: ShiftHoraireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShiftHoraireUpdateComponent,
    resolve: {
      shiftHoraire: ShiftHoraireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shiftHoraireRoute)],
  exports: [RouterModule],
})
export class ShiftHoraireRoutingModule {}
