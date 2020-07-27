import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MentalCentralSharedModule } from 'app/shared/shared.module';
import { VisitComponent } from './visit.component';
import { VisitDetailComponent } from './visit-detail.component';
import { VisitUpdateComponent } from './visit-update.component';
import { VisitDeleteDialogComponent } from './visit-delete-dialog.component';
import { visitRoute } from './visit.route';
import { UserIdPipe } from './user-id.pipe';

@NgModule({
  imports: [MentalCentralSharedModule, RouterModule.forChild(visitRoute)],
  declarations: [VisitComponent, VisitDetailComponent, VisitUpdateComponent, VisitDeleteDialogComponent, UserIdPipe],
  entryComponents: [VisitDeleteDialogComponent],
})
export class MentalCentralVisitModule {}
