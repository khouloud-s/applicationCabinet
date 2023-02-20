import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IShiftHoraire, NewShiftHoraire } from '../shift-horaire.model';

export type PartialUpdateShiftHoraire = Partial<IShiftHoraire> & Pick<IShiftHoraire, 'id'>;

type RestOf<T extends IShiftHoraire | NewShiftHoraire> = Omit<T, 'timeStart' | 'timeEnd'> & {
  timeStart?: string | null;
  timeEnd?: string | null;
};

export type RestShiftHoraire = RestOf<IShiftHoraire>;

export type NewRestShiftHoraire = RestOf<NewShiftHoraire>;

export type PartialUpdateRestShiftHoraire = RestOf<PartialUpdateShiftHoraire>;

export type EntityResponseType = HttpResponse<IShiftHoraire>;
export type EntityArrayResponseType = HttpResponse<IShiftHoraire[]>;

@Injectable({ providedIn: 'root' })
export class ShiftHoraireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/shift-horaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(shiftHoraire: NewShiftHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shiftHoraire);
    return this.http
      .post<RestShiftHoraire>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(shiftHoraire: IShiftHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shiftHoraire);
    return this.http
      .put<RestShiftHoraire>(`${this.resourceUrl}/${this.getShiftHoraireIdentifier(shiftHoraire)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(shiftHoraire: PartialUpdateShiftHoraire): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(shiftHoraire);
    return this.http
      .patch<RestShiftHoraire>(`${this.resourceUrl}/${this.getShiftHoraireIdentifier(shiftHoraire)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestShiftHoraire>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestShiftHoraire[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getShiftHoraireIdentifier(shiftHoraire: Pick<IShiftHoraire, 'id'>): number {
    return shiftHoraire.id;
  }

  compareShiftHoraire(o1: Pick<IShiftHoraire, 'id'> | null, o2: Pick<IShiftHoraire, 'id'> | null): boolean {
    return o1 && o2 ? this.getShiftHoraireIdentifier(o1) === this.getShiftHoraireIdentifier(o2) : o1 === o2;
  }

  addShiftHoraireToCollectionIfMissing<Type extends Pick<IShiftHoraire, 'id'>>(
    shiftHoraireCollection: Type[],
    ...shiftHorairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const shiftHoraires: Type[] = shiftHorairesToCheck.filter(isPresent);
    if (shiftHoraires.length > 0) {
      const shiftHoraireCollectionIdentifiers = shiftHoraireCollection.map(
        shiftHoraireItem => this.getShiftHoraireIdentifier(shiftHoraireItem)!
      );
      const shiftHorairesToAdd = shiftHoraires.filter(shiftHoraireItem => {
        const shiftHoraireIdentifier = this.getShiftHoraireIdentifier(shiftHoraireItem);
        if (shiftHoraireCollectionIdentifiers.includes(shiftHoraireIdentifier)) {
          return false;
        }
        shiftHoraireCollectionIdentifiers.push(shiftHoraireIdentifier);
        return true;
      });
      return [...shiftHorairesToAdd, ...shiftHoraireCollection];
    }
    return shiftHoraireCollection;
  }

  protected convertDateFromClient<T extends IShiftHoraire | NewShiftHoraire | PartialUpdateShiftHoraire>(shiftHoraire: T): RestOf<T> {
    return {
      ...shiftHoraire,
      timeStart: shiftHoraire.timeStart?.toJSON() ?? null,
      timeEnd: shiftHoraire.timeEnd?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restShiftHoraire: RestShiftHoraire): IShiftHoraire {
    return {
      ...restShiftHoraire,
      timeStart: restShiftHoraire.timeStart ? dayjs(restShiftHoraire.timeStart) : undefined,
      timeEnd: restShiftHoraire.timeEnd ? dayjs(restShiftHoraire.timeEnd) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestShiftHoraire>): HttpResponse<IShiftHoraire> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestShiftHoraire[]>): HttpResponse<IShiftHoraire[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
