import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MyPatientsComponent } from '../my-patients.component';

const MyPatientsRoute: Routes = [
  {
    path: '',
    component: MyPatientsComponent,
    data: {
      defaultSort: 'id,asc',
      resolve: {
      
      },
    },
    canActivate: [UserRouteAccessService],
  },
 
];

@NgModule({
  imports: [RouterModule.forChild( MyPatientsRoute)],
  exports: [RouterModule],
})
export class MyPatientRoutingModule { }
