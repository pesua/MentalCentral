import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IVisit } from 'app/shared/model/visit.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { VisitService } from './visit.service';
import { VisitDeleteDialogComponent } from './visit-delete-dialog.component';
import { UserService } from '../../core/user/user.service';
import * as moment from 'moment';

@Component({
  selector: 'jhi-visit',
  templateUrl: './visit.component.html',
})
export class VisitComponent implements OnInit, OnDestroy {
  visits: IVisit[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  currentSearch: string;
  id!: bigint;

  constructor(
    protected visitService: VisitService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.visits = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
    this.currentSearch =
      this.activatedRoute.snapshot && this.activatedRoute.snapshot.queryParams['search']
        ? this.activatedRoute.snapshot.queryParams['search']
        : '';
  }

  loadAll(): void {
    if (this.currentSearch) {
      this.visitService
        .search({
          query: this.currentSearch,
          page: this.page,
          size: this.itemsPerPage,
          sort: this.sort(),
        })
        .subscribe((res: HttpResponse<IVisit[]>) => this.paginateVisits(res.body, res.headers));
      return;
    }

    this.visitService
      .query({
        'time.greaterOrEqualThan': moment(new Date().setHours(0, 0, 0, 0)).toJSON(),
        'time.lessOrEqualThan': moment(new Date().setHours(23, 59, 59, 999)).toJSON(),
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IVisit[]>) => this.paginateVisits(res.body, res.headers));

    this.userService.currentId().subscribe((res: bigint) => (this.id = res));
  }

  reset(): void {
    this.page = 0;
    this.visits = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  search(query: string): void {
    this.visits = [];
    this.links = {
      last: 0,
    };
    this.page = 0;
    if (query) {
      this.predicate = '_score';
      this.ascending = false;
    } else {
      this.predicate = 'id';
      this.ascending = true;
    }
    this.currentSearch = query;
    this.loadAll();
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
    this.eventSubscriber = this.eventManager.subscribe('visitListModification', () => this.reset());
  }

  delete(visit: IVisit): void {
    const modalRef = this.modalService.open(VisitDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.visit = visit;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateVisits(data: IVisit[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.visits.push(data[i]);
      }
    }
  }

  @HostListener('document:keydown', ['$event'])
  help(event: KeyboardEvent): void {
    if (event.key === 'F2')
      window.location.href = 'https://www.notion.so/Mental-Central-d342bba135c6425ab6a4483452e84a1a#3b80964c4b31415b95ba30598bae59e6';
  }
}
