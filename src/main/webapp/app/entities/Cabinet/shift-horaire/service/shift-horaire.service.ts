import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShiftHoraire, getShiftHoraireIdentifier } from '../shift-horaire.model';

export type EntityResponseType = HttpResponse<IShiftHoraire>;
export type EntityArrayResponseType = HttpResponse<IShiftHoraire[]>;

@Injectable({ providedIn: 'root' })
export class ShiftHoraireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shift-horaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(shiftHoraire: IShiftHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shiftHoraire);
    return this.http
      .post<IShiftHoraire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(shiftHoraire: IShiftHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shiftHoraire);
    return this.http
      .put<IShiftHoraire>(`${this.resourceUrl}/${getShiftHoraireIdentifier(shiftHoraire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(shiftHoraire: IShiftHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shiftHoraire);
    return this.http
      .patch<IShiftHoraire>(`${this.resourceUrl}/${getShiftHoraireIdentifier(shiftHoraire) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IShiftHoraire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IShiftHoraire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addShiftHoraireToCollectionIfMissing(
    shiftHoraireCollection: IShiftHoraire[],
    ...shiftHorairesToCheck: (IShiftHoraire | null | undefined)[]
  ): IShiftHoraire[] {
    const shiftHoraires: IShiftHoraire[] = shiftHorairesToCheck.filter(isPresent);
    if (shiftHoraires.length > 0) {
      const shiftHoraireCollectionIdentifiers = shiftHoraireCollection.map(
        shiftHoraireItem => getShiftHoraireIdentifier(shiftHoraireItem)!
      );
      const shiftHorairesToAdd = shiftHoraires.filter(shiftHoraireItem => {
        const shiftHoraireIdentifier = getShiftHoraireIdentifier(shiftHoraireItem);
        if (shiftHoraireIdentifier == null || shiftHoraireCollectionIdentifiers.includes(shiftHoraireIdentifier)) {
          return false;
        }
        shiftHoraireCollectionIdentifiers.push(shiftHoraireIdentifier);
        return true;
      });
      return [...shiftHorairesToAdd, ...shiftHoraireCollection];
    }
    return shiftHoraireCollection;
  }

  protected convertDateFromClient(shiftHoraire: IShiftHoraire): IShiftHoraire {
    return Object.assign({}, shiftHoraire, {
      timeStart: shiftHoraire.timeStart?.isValid() ? shiftHoraire.timeStart.toJSON() : undefined,
      timeEnd: shiftHoraire.timeEnd?.isValid() ? shiftHoraire.timeEnd.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.timeStart = res.body.timeStart ? dayjs(res.body.timeStart) : undefined;
      res.body.timeEnd = res.body.timeEnd ? dayjs(res.body.timeEnd) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((shiftHoraire: IShiftHoraire) => {
        shiftHoraire.timeStart = shiftHoraire.timeStart ? dayjs(shiftHoraire.timeStart) : undefined;
        shiftHoraire.timeEnd = shiftHoraire.timeEnd ? dayjs(shiftHoraire.timeEnd) : undefined;
      });
    }
    return res;
  }
}
