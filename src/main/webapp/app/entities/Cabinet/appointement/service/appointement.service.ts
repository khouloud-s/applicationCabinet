import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAppointement, getAppointementIdentifier } from '../appointement.model';

export type EntityResponseType = HttpResponse<IAppointement>;
export type EntityArrayResponseType = HttpResponse<IAppointement[]>;

@Injectable({ providedIn: 'root' })
export class AppointementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/appointements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(appointement: IAppointement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointement);
    return this.http
      .post<IAppointement>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(appointement: IAppointement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointement);
    return this.http
      .put<IAppointement>(`${this.resourceUrl}/${getAppointementIdentifier(appointement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(appointement: IAppointement): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(appointement);
    return this.http
      .patch<IAppointement>(`${this.resourceUrl}/${getAppointementIdentifier(appointement) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IAppointement>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IAppointement[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addAppointementToCollectionIfMissing(
    appointementCollection: IAppointement[],
    ...appointementsToCheck: (IAppointement | null | undefined)[]
  ): IAppointement[] {
    const appointements: IAppointement[] = appointementsToCheck.filter(isPresent);
    if (appointements.length > 0) {
      const appointementCollectionIdentifiers = appointementCollection.map(
        appointementItem => getAppointementIdentifier(appointementItem)!
      );
      const appointementsToAdd = appointements.filter(appointementItem => {
        const appointementIdentifier = getAppointementIdentifier(appointementItem);
        if (appointementIdentifier == null || appointementCollectionIdentifiers.includes(appointementIdentifier)) {
          return false;
        }
        appointementCollectionIdentifiers.push(appointementIdentifier);
        return true;
      });
      return [...appointementsToAdd, ...appointementCollection];
    }
    return appointementCollection;
  }

  protected convertDateFromClient(appointement: IAppointement): IAppointement {
    return Object.assign({}, appointement, {
      date: appointement.date?.isValid() ? appointement.date.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? dayjs(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((appointement: IAppointement) => {
        appointement.date = appointement.date ? dayjs(appointement.date) : undefined;
      });
    }
    return res;
  }
}
