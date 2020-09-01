import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPatient } from 'app/shared/model/patient.model';
import { VisitService } from '../visit/visit.service';

@Component({
  selector: 'jhi-patient-detail',
  templateUrl: './patient-detail.component.html',
})
export class PatientDetailComponent implements OnInit {
  patient: IPatient | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected visitService: VisitService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => (this.patient = patient));
  }

  previousState(): void {
    window.history.back();
  }
}
