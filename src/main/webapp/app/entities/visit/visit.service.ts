import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption, SearchWithPagination } from 'app/shared/util/request-util';
import { IVisit } from 'app/shared/model/visit.model';

type EntityResponseType = HttpResponse<IVisit>;
type EntityArrayResponseType = HttpResponse<IVisit[]>;

@Injectable({ providedIn: 'root' })
export class VisitService {
  public resourceUrl = SERVER_API_URL + 'api/visits';
  public resourceSearchUrl = SERVER_API_URL + 'api/_search/visits';

  constructor(protected http: HttpClient) {}

  create(visit: IVisit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visit);
    return this.http
      .post<IVisit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(visit: IVisit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(visit);
    return this.http
      .put<IVisit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVisit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVisit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVisit[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  protected convertDateFromClient(visit: IVisit): IVisit {
    const copy: IVisit = Object.assign({}, visit, {
      time: visit.time && visit.time.isValid() ? visit.time.toJSON() : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.time = res.body.time ? moment(res.body.time) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((visit: IVisit) => {
        visit.time = visit.time ? moment(visit.time) : undefined;
      });
    }
    return res;
  }
}
