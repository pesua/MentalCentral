import { Component, AfterViewInit, ElementRef, ViewChild, HostListener } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';

import { PasswordResetInitService } from './password-reset-init.service';

@Component({
  selector: 'jhi-password-reset-init',
  templateUrl: './password-reset-init.component.html',
})
export class PasswordResetInitComponent implements AfterViewInit {
  @ViewChild('email', { static: false })
  email?: ElementRef;

  success = false;
  resetRequestForm = this.fb.group({
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
  });

  constructor(private passwordResetInitService: PasswordResetInitService, private fb: FormBuilder) {}

  ngAfterViewInit(): void {
    if (this.email) {
      this.email.nativeElement.focus();
    }
  }

  requestReset(): void {
    this.passwordResetInitService.save(this.resetRequestForm.get(['email'])!.value).subscribe(() => (this.success = true));
  }

  @HostListener('document:keydown', ['$event'])
  help(event: KeyboardEvent): void {
    if (event.key === 'F2')
      window.location.href = 'https://www.notion.so/Mental-Central-d342bba135c6425ab6a4483452e84a1a#5e16bddf35d64f1e869a5b49020863f3';
  }
}
