import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHistory } from 'app/shared/model/history.model';
import { HistoryService } from './history.service';

@Component({
  templateUrl: './history-delete-dialog.component.html',
})
export class HistoryDeleteDialogComponent {
  history?: IHistory;

  constructor(protected historyService: HistoryService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.historyService.delete(id).subscribe(() => {
      this.eventManager.broadcast('historyListModification');
      this.activeModal.close();
    });
  }
}
