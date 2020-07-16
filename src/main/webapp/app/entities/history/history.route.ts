import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IHistory, History } from 'app/shared/model/history.model';
import { HistoryService } from './history.service';
import { HistoryComponent } from './history.component';
import { HistoryDetailComponent } from './history-detail.component';
import { HistoryUpdateComponent } from './history-update.component';

@Injectable({ providedIn: 'root' })
export class HistoryResolve implements Resolve<IHistory> {
  constructor(private service: HistoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHistory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((history: HttpResponse<History>) => {
          if (history.body) {
            return of(history.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new History());
  }
}

export const historyRoute: Routes = [
  {
    path: '',
    component: HistoryComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mentalCentralApp.history.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HistoryDetailComponent,
    resolve: {
      history: HistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mentalCentralApp.history.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HistoryUpdateComponent,
    resolve: {
      history: HistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mentalCentralApp.history.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HistoryUpdateComponent,
    resolve: {
      history: HistoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'mentalCentralApp.history.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
