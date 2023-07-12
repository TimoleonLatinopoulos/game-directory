import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { ValidationErrorComponent } from './components/validation-error/validation-error.component';
import { FormsModule } from '@angular/forms';
import { MultiInputComponent } from './components/multi-input/multi-input.component';

/**
 * Application wide Module
 */
@NgModule({
  declarations: [ValidationErrorComponent, MultiInputComponent],
  imports: [CommonModule, AlertComponent, AlertErrorComponent, FormsModule],
  exports: [CommonModule, NgbModule, FontAwesomeModule, AlertComponent, AlertErrorComponent, ValidationErrorComponent, MultiInputComponent],
})
export default class SharedModule {}
