import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IPatient } from '../../shared/model/patient.model';
import { VisitService } from '../visit/visit.service';
import { HttpResponse } from '@angular/common/http';
import { IVisit } from '../../shared/model/visit.model';

@Component({
  selector: 'jhi-patient-visits',
  templateUrl: './patient-visits.component.html',
})
export class PatientVisitsComponent implements OnInit {
  patient: IPatient | null = null;
  allVisits: IVisit[];

  constructor(protected activatedRoute: ActivatedRoute, protected visitService: VisitService) {
    this.allVisits = [];
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => (this.patient = patient));

    this.visitService.query().subscribe((res: HttpResponse<IVisit[]>) => (this.allVisits = res.body || []));
  }

  previousState(): void {
    window.history.back();
  }
}
