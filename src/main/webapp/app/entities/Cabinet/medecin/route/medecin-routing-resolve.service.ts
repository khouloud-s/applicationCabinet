import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedecin } from '../medecin.model';
import { MedecinService } from '../service/medecin.service';

@Injectable({ providedIn: 'root' })
export class MedecinRoutingResolveService implements Resolve<IMedecin | null> {
  constructor(protected service: MedecinService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedecin | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((medecin: HttpResponse<IMedecin>) => {
          if (medecin.body) {
            return of(medecin.body);
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
