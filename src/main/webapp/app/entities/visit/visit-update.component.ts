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
import { JhiDataUtils, JhiEventManager, JhiEventWithContent, JhiFileLoadError } from 'ng-jhipster';
import { AlertError } from '../../shared/alert/alert-error.model';

type SelectableEntity = IUser | IPatient;

@Component({
  selector: 'jhi-visit-update',
  templateUrl: './visit-update.component.html',
})
export class VisitUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  patients: IPatient[] = [];
  visits!: IVisit[];
  fileName?: string = '';

  editForm = this.fb.group({
    id: [],
    type: [null, [Validators.required]],
    time: [null, [Validators.required]],
    therapy: [],
    file: [],
    fileContentType: [],
    fileName: [],
    user: [null, Validators.required],
    patient: [null, Validators.required],
    note: [],
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    protected eventManager: JhiEventManager,
    protected visitService: VisitService,
    protected userService: UserService,
    protected patientService: PatientService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ visit }) => {
      if (!visit.id) {
        visit.time = moment().startOf('day');
      }

      this.updateForm(visit);

      this.userService.findAllDoctors().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.visitService.query().subscribe((res: HttpResponse<IVisit[]>) => (this.visits = res.body || []));

      this.fileName = visit.fileName;

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
      file: visit.file,
      fileContentType: visit.fileContentType,
      patient: visit.patient,
      note: visit.note,
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
      fileContentType: this.editForm.get(['fileContentType'])!.value,
      file: this.editForm.get(['file'])!.value,
      fileName: this.fileName,
      patient: this.editForm.get(['patient'])!.value,
      note: this.editForm.get(['note'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVisit>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType: string, base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
    this.dataUtils.downloadFile(contentType, base64String, this.fileName ? this.fileName : 'new_file');
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.fileName = (event.currentTarget as HTMLInputElement)?.files?.item(0)?.name;
    if ((event.currentTarget as HTMLInputElement)?.files != null) {
      this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
        this.eventManager.broadcast(
          new JhiEventWithContent<AlertError>('mentalCentralApp.error', { ...err, key: 'error.file.' + err.key })
        );
      });
    }
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
