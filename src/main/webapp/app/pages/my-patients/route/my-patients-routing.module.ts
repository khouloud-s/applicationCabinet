import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PatientDetailComponent } from 'app/entities/Cabinet/patient/detail/patient-detail.component';
import { PatientRoutingResolveService } from 'app/entities/Cabinet/patient/route/patient-routing-resolve.service';
import { PatientUpdateComponent } from 'app/entities/Cabinet/patient/update/patient-update.component';
import { MyPatientsComponent } from '../my-patients.component';

const MyPatientsRoute: Routes = [
  {
    path: '',
    component: MyPatientsComponent,
    data: {
      defaultSort: 'id,asc',
      resolve: {},
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatientDetailComponent,
    resolve: {
      patient: PatientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatientUpdateComponent,
    resolve: {
      patient: PatientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatientUpdateComponent,
    resolve: {
      patient: PatientRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(MyPatientsRoute)],
  exports: [RouterModule],
})
export class MyPatientRoutingModule {}
