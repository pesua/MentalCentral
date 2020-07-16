import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';

import { IVisit, Visit } from 'app/shared/model/visit.model';
import { VisitService } from './visit.service';
import { IDoctor } from 'app/shared/model/doctor.model';
import { DoctorService } from 'app/entities/doctor/doctor.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';

type SelectableEntity = IDoctor | IPatient;

@Component({
  selector: 'jhi-visit-update',
  templateUrl: './visit-update.component.html',
})
export class VisitUpdateComponent implements OnInit {
  isSaving = false;
  doctors: IDoctor[] = [];
  patients: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    time: [null, [Validators.required]],
    teraphy: [null, [Validators.required]],
    doctorId: [null, Validators.required],
    patientId: [null, Validators.required],
  });

  constructor(
    protected visitService: VisitService,
    protected doctorService: DoctorService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visit }) => {
      if (!visit.id) {
        const today = moment().startOf('day');
        visit.time = today;
      }

      this.updateForm(visit);

      this.doctorService.query().subscribe((res: HttpResponse<IDoctor[]>) => (this.doctors = res.body || []));

      this.patientService.query().subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body || []));
    });
  }

  updateForm(visit: IVisit): void {
    this.editForm.patchValue({
      id: visit.id,
      type: visit.type,
      time: visit.time ? visit.time.format(DATE_TIME_FORMAT) : null,
      teraphy: visit.teraphy,
      doctorId: visit.doctorId,
      patientId: visit.patientId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const visit = this.createFromForm();
    if (visit.id !== undefined) {
      this.subscribeToSaveResponse(this.visitService.update(visit));
    } else {
      this.subscribeToSaveResponse(this.visitService.create(visit));
    }
  }

  private createFromForm(): IVisit {
    return {
      ...new Visit(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      time: this.editForm.get(['time'])!.value ? moment(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
      teraphy: this.editForm.get(['teraphy'])!.value,
      doctorId: this.editForm.get(['doctorId'])!.value,
      patientId: this.editForm.get(['patientId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisit>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
