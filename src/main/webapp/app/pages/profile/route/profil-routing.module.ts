import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProfileComponent } from '../profile.component';


const ProfilRoute: Routes = [
  {
    path: '',
    component: ProfileComponent,
    data: {
      defaultSort: 'id,asc',
      resolve: {
       
      },
    },
    canActivate: [UserRouteAccessService],
  },
 
];

@NgModule({
  imports: [RouterModule.forChild(ProfilRoute)],
  exports: [RouterModule],
})
export class ProfilRoutingModule { }
