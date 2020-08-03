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
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';
type SelectableEntity = IUser | IPatient;

@Component({
  selector: 'jhi-visit-update',
  templateUrl: './visit-update.component.html',
})
export class VisitUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  patients: IPatient[] = [];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    time: [null, [Validators.required]],
    therapy: [],
    user: [null, Validators.required],
    patient: [null, Validators.required],
  });

  constructor(
    protected visitService: VisitService,
    protected userService: UserService,
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

      this.userService.findAllDoctors().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.patientService.queryWithoutPagination().subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body || []));
    });
  }

  updateForm(visit: IVisit): void {
    this.editForm.patchValue({
      id: visit.id,
      type: visit.type,
      time: visit.time ? visit.time.format(DATE_TIME_FORMAT) : null,
      therapy: visit.therapy,
      user: visit.user,
      patient: visit.patient,
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
      therapy: this.editForm.get(['therapy'])!.value,
      user: this.editForm.get(['user'])!.value,
      patient: this.editForm.get(['patient'])!.value,
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
