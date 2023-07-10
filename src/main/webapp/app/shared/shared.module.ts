import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { ValidationErrorComponent } from './components/validation-error/validation-error.component';

/**
 * Application wide Module
 */
@NgModule({
  declarations: [ValidationErrorComponent],
  imports: [CommonModule, AlertComponent, AlertErrorComponent],
  exports: [CommonModule, NgbModule, FontAwesomeModule, AlertComponent, AlertErrorComponent, ValidationErrorComponent],
})
export default class SharedModule {}
