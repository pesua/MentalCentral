import { NgModule } from '@angular/core';
import { MentalCentralSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { PasswordStrengthBarComponent } from 'app/account/password/password-strength-bar.component';

@NgModule({
  imports: [MentalCentralSharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    PasswordStrengthBarComponent,
  ],
  entryComponents: [LoginModalComponent],
  exports: [
    MentalCentralSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    PasswordStrengthBarComponent,
  ],
})
export class MentalCentralSharedModule {}
