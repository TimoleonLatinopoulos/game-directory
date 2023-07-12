import { Component, Input, OnInit } from '@angular/core';
import { AbstractControl } from '@angular/forms';

@Component({
  selector: 'jhi-validation-error',
  templateUrl: './validation-error.component.html',
  styleUrls: ['./validation-error.component.scss'],
})
export class ValidationErrorComponent implements OnInit {
  @Input()
  public abstractControl: AbstractControl | null | undefined;

  @Input()
  public showOnlyOnTouched = true;

  @Input()
  public absolutePosition = false;

  @Input() set validationMessages(validationMessages: any) {
    Object.assign(this._validationMessages, validationMessages);
  }

  public maxPriority = 0;

  public _validationMessages = {
    required: { message: 'The field is required', priority: 0 },
    pattern: { message: 'The field is not valid', priority: 2 },
    min: { message: 'The value you gave is too small', priority: 1 },
    max: { message: 'The value you gave is too big', priority: 1 },
    minlength: { message: 'The number of characters is too small', priority: 1 },
    maxlength: { message: 'The number of characters is too big', priority: 1 },
    futureDate: { message: 'The date cannot be later than today', priority: 1 },
    pastDate: { message: 'The date cannot be today or earlier', priority: 1 },
    minDate: { message: 'Invalid date', priority: 1 },
    maxDate: { message: 'Invalid date', priority: 1 },
  };

  constructor() {
    this.abstractControl = null;
  }

  ngOnInit(): void {
    return;
  }

  public getErrors(): any {
    this.maxPriority = 99;
    if (this.abstractControl?.errors) {
      const keys = Object.keys(this.abstractControl.errors);

      keys.forEach(key => {
        if (
          this._validationMessages[key as keyof typeof this._validationMessages] != undefined &&
          this._validationMessages[key as keyof typeof this._validationMessages].priority < this.maxPriority
        ) {
          this.maxPriority = this._validationMessages[key as keyof typeof this._validationMessages].priority;
        }
      });

      return keys;
    }
  }

  public checkForError(key: any): boolean {
    if (this._validationMessages[key as keyof typeof this._validationMessages] != undefined) {
      return (
        this._validationMessages[key as keyof typeof this._validationMessages].priority === this.maxPriority ||
        !this._validationMessages[key as keyof typeof this._validationMessages].priority
      );
    }
    return false;
    // this._validationMessages[key]?.alwaysShow === true
  }

  public getErrorMessage(key: any): string {
    return this._validationMessages[key as keyof typeof this._validationMessages].message;
  }
}
