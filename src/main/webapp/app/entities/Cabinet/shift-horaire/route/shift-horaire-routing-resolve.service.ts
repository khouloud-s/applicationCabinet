import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShiftHoraire, ShiftHoraire } from '../shift-horaire.model';
import { ShiftHoraireService } from '../service/shift-horaire.service';

@Injectable({ providedIn: 'root' })
export class ShiftHoraireRoutingResolveService implements Resolve<IShiftHoraire> {
  constructor(protected service: ShiftHoraireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShiftHoraire> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shiftHoraire: HttpResponse<ShiftHoraire>) => {
          if (shiftHoraire.body) {
            return of(shiftHoraire.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShiftHoraire());
  }
}