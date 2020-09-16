import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

import { LANGUAGES } from 'app/core/language/language.constants';
import { User } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { RegisterService } from '../../account/register/register.service';
import { UserExtraService } from '../../entities/user-extra/user-extra.service';

@Component({
  selector: 'jhi-user-mgmt-update',
  templateUrl: './user-management-update.component.html',
})
export class UserManagementUpdateComponent implements OnInit {
  user!: User;
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;
  doNotMatch = false;

  editForm = this.fb.group({
    id: [],
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    middleName: ['', [Validators.maxLength(50)]],
    phoneNumber: ['', [Validators.pattern('[+]380[0-9]{9}')]],
    degree: ['', [Validators.maxLength(50)]],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.minLength(4), Validators.maxLength(50)]],
    activated: [],
    langKey: [],
    authorities: [],
  });

  constructor(
    private userService: UserService,
    private registerService: RegisterService,
    private userExtraService: UserExtraService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      if (user) {
        this.user = user;

        if (this.user.id === undefined) {
          this.user.activated = true;
        } else {
          this.userExtraService.find(user.id).subscribe(userExtra => {
            user.middleName = userExtra.body!.middleName;
            user.phoneNumber = userExtra.body!.phoneNumber;
            user.degree = userExtra.body!.degree;

            this.updateForm(user);
          });
        }
      }
    });
    this.userService.authorities().subscribe(authorities => {
      this.authorities = authorities;
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    this.doNotMatch = false;

    this.updateUser(this.user);

    if (this.user.id !== undefined) {
      this.userService.update(this.user).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    } else {
      if (this.user.password !== this.editForm.get(['confirmPassword'])!.value) {
        this.doNotMatch = true;
      } else {
        this.registerService.save(this.user).subscribe(
          () => this.onSaveSuccess(),
          () => this.onSaveError()
        );
      }
    }
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      middleName: user.middleName,
      email: user.email,
      phoneNumber: user.phoneNumber,
      degree: user.degree,
      activated: user.activated,
      langKey: user.langKey,
      authorities: user.authorities,
    });
  }

  private updateUser(user: User): void {
    user.login = this.editForm.get(['login'])!.value;
    user.firstName = this.editForm.get(['firstName'])!.value;
    user.lastName = this.editForm.get(['lastName'])!.value;
    user.middleName = this.editForm.get(['middleName'])!.value;
    user.email = this.editForm.get(['email'])!.value;
    user.phoneNumber = this.editForm.get(['phoneNumber'])!.value;
    user.password = this.editForm.get(['password'])!.value;
    user.activated = this.editForm.get(['activated'])!.value;
    user.langKey = this.editForm.get(['langKey'])!.value;
    user.authorities = this.editForm.get(['authorities'])!.value;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
