import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MentalCentralSharedModule } from 'app/shared/shared.module';
import { PatientComponent } from './patient.component';
import { PatientDetailComponent } from './patient-detail.component';
import { PatientUpdateComponent } from './patient-update.component';
import { PatientDeleteDialogComponent } from './patient-delete-dialog.component';
import { patientRoute } from './patient.route';
import { PatientVisitsComponent } from './patient-visits.component';
import { FIlterPipe } from './filter-pipe.pipe';
import { FilterFieldsPipe } from './filter-fields.pipe';

@NgModule({
  imports: [MentalCentralSharedModule, RouterModule.forChild(patientRoute)],
  declarations: [
    PatientComponent,
    PatientDetailComponent,
    PatientUpdateComponent,
    PatientDeleteDialogComponent,
    PatientVisitsComponent,
    FIlterPipe,
    FilterFieldsPipe,
  ],
  entryComponents: [PatientDeleteDialogComponent],
})
export class MentalCentralPatientModule {}
