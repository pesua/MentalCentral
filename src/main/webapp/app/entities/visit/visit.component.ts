import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisit } from 'app/shared/model/visit.model';
import { VisitService } from './visit.service';
import { VisitDeleteDialogComponent } from './visit-delete-dialog.component';

@Component({
  selector: 'jhi-visit',
  templateUrl: './visit.component.html',
})
export class VisitComponent implements OnInit, OnDestroy {
  visits?: IVisit[];
  eventSubscriber?: Subscription;

  constructor(
    protected dataUtils: JhiDataUtils,
    protected visitService: VisitService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.visitService.query().subscribe((res: HttpResponse<IVisit[]>) => (this.visits = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInVisits();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IVisit): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInVisits(): void {
    this.eventSubscriber = this.eventManager.subscribe('visitListModification', () => this.loadAll());
  }

  openFile(base64String: string, fileName?: string): void {
    this.dataUtils.openFile('application/pdf', base64String);
    this.dataUtils.downloadFile('application/pdf', base64String, fileName ? fileName : 'new_file');
  }

  delete(visit: IVisit): void {
    const modalRef = this.modalService.open(VisitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.visit = visit;
  }
}
