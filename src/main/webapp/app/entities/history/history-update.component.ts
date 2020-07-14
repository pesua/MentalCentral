import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IHistory, History } from 'app/shared/model/history.model';
import { HistoryService } from './history.service';
import { IPatient } from 'app/shared/model/patient.model';
import { PatientService } from 'app/entities/patient/patient.service';

@Component({
  selector: 'jhi-history-update',
  templateUrl: './history-update.component.html',
})
export class HistoryUpdateComponent implements OnInit {
  isSaving = false;
  patients: IPatient[] = [];
  dateRecordDp: any;

  editForm = this.fb.group({
    id: [],
    dateRecord: [null, [Validators.required]],
    type: [null, [Validators.required]],
    info: [null, [Validators.required]],
    patientId: [null, Validators.required],
  });

  constructor(
    protected historyService: HistoryService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ history }) => {
      this.updateForm(history);

      this.patientService.query().subscribe((res: HttpResponse<IPatient[]>) => (this.patients = res.body || []));
    });
  }

  updateForm(history: IHistory): void {
    this.editForm.patchValue({
      id: history.id,
      dateRecord: history.dateRecord,
      type: history.type,
      info: history.info,
      patientId: history.patientId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const history = this.createFromForm();
    if (history.id !== undefined) {
      this.subscribeToSaveResponse(this.historyService.update(history));
    } else {
      this.subscribeToSaveResponse(this.historyService.create(history));
    }
  }

  private createFromForm(): IHistory {
    return {
      ...new History(),
      id: this.editForm.get(['id'])!.value,
      dateRecord: this.editForm.get(['dateRecord'])!.value,
      type: this.editForm.get(['type'])!.value,
      info: this.editForm.get(['info'])!.value,
      patientId: this.editForm.get(['patientId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistory>>): void {
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

  trackById(index: number, item: IPatient): any {
    return item.id;
  }
}
