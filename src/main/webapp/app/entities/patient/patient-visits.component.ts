import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IPatient } from '../../shared/model/patient.model';
import { VisitService } from '../visit/visit.service';
import { HttpResponse } from '@angular/common/http';
import { IVisit, Visit } from '../../shared/model/visit.model';
import { IUser } from '../../core/user/user.model';
import { FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../../core/user/user.service';
import { PatientService } from './patient.service';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from '../../shared/constants/input.constants';
import { Observable } from 'rxjs';

type SelectableEntity = IUser;

@Component({
  selector: 'jhi-patient-visits',
  templateUrl: './patient-visits.component.html',
})
export class PatientVisitsComponent implements OnInit {
  visits!: IVisit[];
  patient!: IPatient;
  isSaving = false;
  users: IUser[] = [];
  therapy = '';
  choosenTime!: Date;
  sortingVisits!: IVisit[];

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    time: [null, [Validators.required]],
    therapy: [],
    user: [null, Validators.required],
    patient: [],
  });

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected visitService: VisitService,
    protected userService: UserService,
    protected patientService: PatientService,
    private fb: FormBuilder
  ) {}

  previousState(): void {
    window.history.back();
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => (this.patient = patient));

    this.userService.findAllDoctors().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

    this.visitService.query().subscribe((res: HttpResponse<IVisit[]>) => (this.visits = res.body || []));
  }

  save(): void {
    this.isSaving = true;
    const visit = this.createFromForm();
    this.subscribeToSaveResponse(this.visitService.create(visit));
  }

  private createFromForm(): IVisit {
    return {
      ...new Visit(),
      id: this.editForm.get(['id'])!.value,
      type: this.editForm.get(['type'])!.value,
      time: this.editForm.get(['time'])!.value ? moment(this.editForm.get(['time'])!.value, DATE_TIME_FORMAT) : undefined,
      therapy: this.therapy,
      user: this.editForm.get(['user'])!.value,
      patient: this.patient,
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

  public isCorrectChosenTime(): boolean {
    this.choosenTime = this.editForm.get(['time'])!.value;
    for (let i = 0; i < this.visits.length; i++) {
      if (
        this.choosenTime.getDate() === this.visits[i].time!.date() &&
        this.choosenTime.getFullYear() === this.visits[i].time!.year() &&
        this.choosenTime.getHours() * 60 +
          this.choosenTime.getMinutes() -
          this.visits[i].time!.hours() * 60 +
          this.visits[i].time!.minutes() <=
          60 &&
        this.choosenTime.getHours() * 60 +
          this.choosenTime.getMinutes() -
          this.visits[i].time!.hours() * 60 +
          this.visits[i].time!.minutes() >=
          -60
      ) {
        return true;
      }
    }
    return false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
