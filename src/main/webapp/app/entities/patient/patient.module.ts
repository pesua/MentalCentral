import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MentalCentralSharedModule } from 'app/shared/shared.module';
import { PatientComponent } from './patient.component';
import { PatientDetailComponent } from './patient-detail.component';
import { PatientUpdateComponent } from './patient-update.component';
import { PatientDeleteDialogComponent } from './patient-delete-dialog.component';
import { patientRoute } from './patient.route';
import { PatientVisitsComponent } from './patient-visits.component';
import { FilterVisitsPipe } from './filter-visits.pipe';
import { ScrollingModule } from '@angular/cdk/scrolling';

@NgModule({
  imports: [MentalCentralSharedModule, RouterModule.forChild(patientRoute), ScrollingModule],
  declarations: [
    PatientComponent,
    PatientDetailComponent,
    PatientUpdateComponent,
    PatientDeleteDialogComponent,
    PatientVisitsComponent,
    FilterVisitsPipe,
  ],
  entryComponents: [PatientDeleteDialogComponent],
})
export class MentalCentralPatientModule {}
