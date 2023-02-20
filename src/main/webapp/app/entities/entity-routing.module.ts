import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'role',
        data: { pageTitle: 'Roles' },
        loadChildren: () => import('./Cabinet/role/role.module').then(m => m.CabinetRoleModule),
      },
      {
        path: 'patient',
        data: { pageTitle: 'Patients' },
        loadChildren: () => import('./Cabinet/patient/patient.module').then(m => m.CabinetPatientModule),
      },
      {
        path: 'medecin',
        data: { pageTitle: 'Medecins' },
        loadChildren: () => import('./Cabinet/medecin/medecin.module').then(m => m.CabinetMedecinModule),
      },
      {
        path: 'appointement',
        data: { pageTitle: 'Appointements' },
        loadChildren: () => import('./Cabinet/appointement/appointement.module').then(m => m.CabinetAppointementModule),
      },
      {
        path: 'shift-horaire',
        data: { pageTitle: 'ShiftHoraires' },
        loadChildren: () => import('./Cabinet/shift-horaire/shift-horaire.module').then(m => m.CabinetShiftHoraireModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
