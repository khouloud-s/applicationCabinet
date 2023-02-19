import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AllPatientsComponent } from '../all-patients.component';


const AllPatientRoute: Routes = [
  {
    path: '',
    component: AllPatientsComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  }
];

@NgModule({
  imports: [RouterModule.forChild(AllPatientRoute)],
  exports: [RouterModule],
})
export class AllPatientRoutingModule { }
