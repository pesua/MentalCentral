import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MentalCentralSharedModule } from 'app/shared/shared.module';
import { VisitComponent } from './visit.component';
import { VisitDetailComponent } from './visit-detail.component';
import { VisitUpdateComponent } from './visit-update.component';
import { VisitDeleteDialogComponent } from './visit-delete-dialog.component';
import { visitRoute } from './visit.route';
import { FilterVisitsByPatientIdPipe } from '../visit/filter-visits-by-patient-id.pipe';

import { ScrollingModule } from '@angular/cdk/scrolling';
import { FilterVisitsByDatePipe } from './filter-visits-by-date.pipe';

@NgModule({
  imports: [MentalCentralSharedModule, RouterModule.forChild(visitRoute), ScrollingModule],
  declarations: [
    VisitComponent,
    VisitDetailComponent,
    VisitUpdateComponent,
    VisitDeleteDialogComponent,
    FilterVisitsByPatientIdPipe,
    FilterVisitsByDatePipe,
  ],
  entryComponents: [VisitDeleteDialogComponent],
})
export class MentalCentralVisitModule {}
