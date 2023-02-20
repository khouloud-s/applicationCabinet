import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAppointement } from '../appointement.model';
import { AppointementService } from '../service/appointement.service';

@Injectable({ providedIn: 'root' })
export class AppointementRoutingResolveService implements Resolve<IAppointement | null> {
  constructor(protected service: AppointementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IAppointement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((appointement: HttpResponse<IAppointement>) => {
          if (appointement.body) {
            return of(appointement.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
