import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IPatient, Patient } from 'app/shared/model/patient.model';
import { PatientService } from './patient.service';
import * as moment from 'moment';

@Component({
  selector: 'jhi-patient-update',
  templateUrl: './patient-update.component.html',
})
export class PatientUpdateComponent implements OnInit {
  isSaving = false;
  birthDateDp: any;

  editForm = this.fb.group({
    id: [],
    fullName: [null, [Validators.required]],
    birthDate: [null, [Validators.required]],
    address: [],
    phoneNumber: [null, [Validators.pattern('[+]380[0-9]{9}')]],
    diagnosis: [null],
  });

  constructor(protected patientService: PatientService, protected activatedRoute: ActivatedRoute, private fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ patient }) => {
      this.updateForm(patient);
    });
  }

  updateForm(patient: IPatient): void {
    this.editForm.patchValue({
      id: patient.id,
      fullName: patient.fullName,
      birthDate: patient.birthDate,
      address: patient.address,
      phoneNumber: patient.phoneNumber,
      diagnosis: patient.diagnosis,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;

    const patient = this.createFromForm();
    if (patient.id !== undefined) {
      this.subscribeToSaveResponse(this.patientService.update(patient));
    } else {
      this.subscribeToSaveResponse(this.patientService.create(patient));
    }
  }

  private createFromForm(): IPatient {
    return {
      ...new Patient(),
      id: this.editForm.get(['id'])!.value,
      fullName: this.editForm.get(['fullName'])!.value,
      birthDate: this.editForm.get(['birthDate'])!.value,
      address: this.editForm.get(['address'])!.value,
      phoneNumber: this.editForm.get(['phoneNumber'])!.value,
      diagnosis: this.editForm.get(['diagnosis'])!.value,
    };
  }

  public checkIfOldEnough(): boolean {
    const birthDate: Date = this.editForm.get(['birthDate'])!.value;
    const age = moment().diff(birthDate, 'years');

    if (age < 1) {
      this.editForm.get(['birthDate'])!.setErrors({ notOldEnough: 'Patient has to be at least one year old.\n' });
      return false;
    } else {
      this.editForm.get(['birthDate'])!.setErrors(null);
      return true;
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPatient>>): void {
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
}
