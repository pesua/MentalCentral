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
        path: 'doctor',
        loadChildren: () => import('./doctor/doctor.module').then(m => m.MentalCentralDoctorModule),
      },
      {
        path: 'history',
        loadChildren: () => import('./history/history.module').then(m => m.MentalCentralHistoryModule),
      },
      {
        path: 'visit',
        loadChildren: () => import('./visit/visit.module').then(m => m.MentalCentralVisitModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class MentalCentralEntityModule {}
