import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { MentalCentralSharedModule } from 'app/shared/shared.module';
import { HistoryComponent } from './history.component';
import { HistoryDetailComponent } from './history-detail.component';
import { HistoryUpdateComponent } from './history-update.component';
import { HistoryDeleteDialogComponent } from './history-delete-dialog.component';
import { historyRoute } from './history.route';

@NgModule({
  imports: [MentalCentralSharedModule, RouterModule.forChild(historyRoute)],
  declarations: [HistoryComponent, HistoryDetailComponent, HistoryUpdateComponent, HistoryDeleteDialogComponent],
  entryComponents: [HistoryDeleteDialogComponent],
})
export class MentalCentralHistoryModule {}
