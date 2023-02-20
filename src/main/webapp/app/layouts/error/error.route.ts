import { Routes } from '@angular/router';

import { ErrorComponent } from './error.component';

export const errorRoute: Routes = [
  {
    path: 'error',
    component: ErrorComponent,
    data: {
      pageTitle: 'Page d'erreur !',
    },
  },
  {
    path: 'accessdenied',
    component: ErrorComponent,
    data: {
      pageTitle: 'Page d'erreur !',
      errorMessage: 'Vous n'avez pas les droits pour accéder à cette page.',
    },
  },
  {
    path: '404',
    component: ErrorComponent,
    data: {
      pageTitle: 'Page d'erreur !',
      errorMessage: 'La page n'existe pas.',
    },
  },
  {
    path: '**',
    redirectTo: '/404',
  },
];
