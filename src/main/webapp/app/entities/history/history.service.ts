import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IHistory } from 'app/shared/model/history.model';

type EntityResponseType = HttpResponse<IHistory>;
type EntityArrayResponseType = HttpResponse<IHistory[]>;

@Injectable({ providedIn: 'root' })
export class HistoryService {
  public resourceUrl = SERVER_API_URL + 'api/histories';

  constructor(protected http: HttpClient) {}

  create(history: IHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(history);
    return this.http
      .post<IHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(history: IHistory): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(history);
    return this.http
      .put<IHistory>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IHistory>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IHistory[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(history: IHistory): IHistory {
    const copy: IHistory = Object.assign({}, history, {
      dateRecord: history.dateRecord && history.dateRecord.isValid() ? history.dateRecord.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateRecord = res.body.dateRecord ? moment(res.body.dateRecord) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((history: IHistory) => {
        history.dateRecord = history.dateRecord ? moment(history.dateRecord) : undefined;
      });
    }
    return res;
  }
}
