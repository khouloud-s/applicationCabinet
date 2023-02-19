import { Route } from '@angular/router';

import {SideBarComponent} from  './side-bar.component';

export const sidebarRoute: Route = {
  path: '',
  component:SideBarComponent,
  outlet: 'sidebar',
};
