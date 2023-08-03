import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { ValidationErrorComponent } from './components/validation-error/validation-error.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MultiInputComponent } from './components/multi-input/multi-input.component';
import { MatChipsModule } from '@angular/material/chips';
import { ChipGridInputComponent } from './components/chip-grid-input/chip-grid-input.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCardModule } from '@angular/material/card';
import { SeparateCategoriesPipe } from './pipes/separate-categories.pipe';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SnackBarAlertComponent } from './components/snack-bar-alert/snack-bar-alert.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { DialogComponent } from './components/dialog/dialog.component';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatExpansionModule } from '@angular/material/expansion';

/**
 * Application wide Module
 */
@NgModule({
  declarations: [
    ValidationErrorComponent,
    MultiInputComponent,
    ChipGridInputComponent,
    SeparateCategoriesPipe,
    SnackBarAlertComponent,
    DialogComponent,
  ],
  imports: [
    CommonModule,
    AlertComponent,
    AlertErrorComponent,
    FormsModule,
    FontAwesomeModule,
    MatChipsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatSnackBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatDialogModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatExpansionModule,
  ],
  exports: [
    CommonModule,
    NgbModule,
    FontAwesomeModule,
    AlertComponent,
    AlertErrorComponent,
    ValidationErrorComponent,
    MultiInputComponent,
    ChipGridInputComponent,
    FormsModule,
    ReactiveFormsModule,
    InfiniteScrollModule,
    MatProgressSpinnerModule,
    MatCardModule,
    SeparateCategoriesPipe,
    MatSnackBarModule,
    SnackBarAlertComponent,
    MatDatepickerModule,
    MatNativeDateModule,
    MatSelectModule,
    MatDialogModule,
    MatAutocompleteModule,
    MatCheckboxModule,
    MatExpansionModule,
  ],
})
export default class SharedModule {}
