import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProfilRoutingModule } from './route/profil-routing.module';

@NgModule({
  imports: [SharedModule,ProfilRoutingModule],
  declarations: [],
  entryComponents: [],
})
export class ProfilModule {}
