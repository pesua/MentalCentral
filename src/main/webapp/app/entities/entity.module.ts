import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'patient',
        loadChildren: () => import('./patient/patient.module').then(m => m.MentalCentralPatientModule),
      },
      {
        path: 'visit',
        loadChildren: () => import('./visit/visit.module').then(m => m.MentalCentralVisitModule),
      },
      {
        path: 'user-extra',
        loadChildren: () => import('./user-extra/user-extra.module').then(m => m.MentalCentralUserExtraModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class MentalCentralEntityModule {}
