import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatient } from 'app/shared/model/patient.model';
import { IVisit } from '../../shared/model/visit.model';
import { VisitService } from '../visit/visit.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-patient-detail',
  templateUrl: './patient-detail.component.html',
})
export class PatientDetailComponent implements OnInit {
  patient: IPatient | null = null;
  visits!: IVisit[];

  constructor(protected activatedRoute: ActivatedRoute, protected visitService: VisitService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => (this.patient = patient));

    this.visitService.query().subscribe((res: HttpResponse<IVisit[]>) => (this.visits = res.body || []));
  }

  previousState(): void {
    window.history.back();
  }
}
