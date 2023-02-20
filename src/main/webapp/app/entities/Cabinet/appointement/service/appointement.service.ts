import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppointement, NewAppointement } from '../appointement.model';

export type PartialUpdateAppointement = Partial<IAppointement> & Pick<IAppointement, 'id'>;

type RestOf<T extends IAppointement | NewAppointement> = Omit<T, 'date'> & {
  date?: string | null;
};

export type RestAppointement = RestOf<IAppointement>;

export type NewRestAppointement = RestOf<NewAppointement>;

export type PartialUpdateRestAppointement = RestOf<PartialUpdateAppointement>;

export type EntityResponseType = HttpResponse<IAppointement>;
export type EntityArrayResponseType = HttpResponse<IAppointement[]>;

@Injectable({ providedIn: 'root' })
export class AppointementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/appointements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appointement: NewAppointement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointement);
    return this.http
      .post<RestAppointement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(appointement: IAppointement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointement);
    return this.http
      .put<RestAppointement>(`${this.resourceUrl}/${this.getAppointementIdentifier(appointement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(appointement: PartialUpdateAppointement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointement);
    return this.http
      .patch<RestAppointement>(`${this.resourceUrl}/${this.getAppointementIdentifier(appointement)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAppointement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAppointement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAppointementIdentifier(appointement: Pick<IAppointement, 'id'>): number {
    return appointement.id;
  }

  compareAppointement(o1: Pick<IAppointement, 'id'> | null, o2: Pick<IAppointement, 'id'> | null): boolean {
    return o1 && o2 ? this.getAppointementIdentifier(o1) === this.getAppointementIdentifier(o2) : o1 === o2;
  }

  addAppointementToCollectionIfMissing<Type extends Pick<IAppointement, 'id'>>(
    appointementCollection: Type[],
    ...appointementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const appointements: Type[] = appointementsToCheck.filter(isPresent);
    if (appointements.length > 0) {
      const appointementCollectionIdentifiers = appointementCollection.map(
        appointementItem => this.getAppointementIdentifier(appointementItem)!
      );
      const appointementsToAdd = appointements.filter(appointementItem => {
        const appointementIdentifier = this.getAppointementIdentifier(appointementItem);
        if (appointementCollectionIdentifiers.includes(appointementIdentifier)) {
          return false;
        }
        appointementCollectionIdentifiers.push(appointementIdentifier);
        return true;
      });
      return [...appointementsToAdd, ...appointementCollection];
    }
    return appointementCollection;
  }

  protected convertDateFromClient<T extends IAppointement | NewAppointement | PartialUpdateAppointement>(appointement: T): RestOf<T> {
    return {
      ...appointement,
      date: appointement.date?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restAppointement: RestAppointement): IAppointement {
    return {
      ...restAppointement,
      date: restAppointement.date ? dayjs(restAppointement.date) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAppointement>): HttpResponse<IAppointement> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAppointement[]>): HttpResponse<IAppointement[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
