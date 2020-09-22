import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router, Routes } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IPatient, Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import { PatientComponent } from './patient.component';
import { PatientDetailComponent } from './patient-detail.component';
import { PatientUpdateComponent } from './patient-update.component';
import { PatientVisitsComponent } from './patient-visits.component';
import { PatientUpdateByDoctorComponent } from './patient-update-by-doctor.component';

@Injectable({ providedIn: 'root' })
export class PatientResolve implements Resolve<IPatient> {
  constructor(private service: PatientService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPatient> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((patient: HttpResponse<Patient>) => {
          if (patient.body) {
            return of(patient.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Patient());
  }
}

export const patientRoute: Routes = [
  {
    path: '',
    component: PatientComponent,
    data: {
      authorities: [Authority.ADMIN, Authority.RECEPTION, Authority.DOCTOR],
      pageTitle: 'mentalCentralApp.patient.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PatientDetailComponent,
    resolve: {
      patient: PatientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.RECEPTION, Authority.DOCTOR],
      pageTitle: 'mentalCentralApp.patient.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PatientUpdateComponent,
    resolve: {
      patient: PatientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.RECEPTION, Authority.DOCTOR],
      pageTitle: 'mentalCentralApp.patient.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'newPatient',
    component: PatientUpdateByDoctorComponent,
    resolve: {
      patient: PatientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.DOCTOR],
      pageTitle: 'mentalCentralApp.patient.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PatientUpdateComponent,
    resolve: {
      patient: PatientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.RECEPTION, Authority.DOCTOR],
      pageTitle: 'mentalCentralApp.patient.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/editByDoctor',
    component: PatientUpdateByDoctorComponent,
    resolve: {
      patient: PatientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.DOCTOR],
      pageTitle: 'mentalCentralApp.patient.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/createVisit',
    component: PatientVisitsComponent,
    resolve: {
      patient: PatientResolve,
    },
    data: {
      authorities: [Authority.ADMIN, Authority.RECEPTION, Authority.DOCTOR],
      pageTitle: 'mentalCentralApp.patient.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
