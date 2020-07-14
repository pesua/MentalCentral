import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDoctor } from 'app/shared/model/doctor.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { DoctorService } from './doctor.service';
import { DoctorDeleteDialogComponent } from './doctor-delete-dialog.component';

@Component({
  selector: 'jhi-doctor',
  templateUrl: './doctor.component.html',
})
export class DoctorComponent implements OnInit, OnDestroy {
  doctors: IDoctor[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected doctorService: DoctorService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.doctors = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.doctorService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IDoctor[]>) => this.paginateDoctors(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.doctors = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInDoctors();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IDoctor): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInDoctors(): void {
    this.eventSubscriber = this.eventManager.subscribe('doctorListModification', () => this.reset());
  }

  delete(doctor: IDoctor): void {
    const modalRef = this.modalService.open(DoctorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.doctor = doctor;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateDoctors(data: IDoctor[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.doctors.push(data[i]);
      }
    }
  }
}
