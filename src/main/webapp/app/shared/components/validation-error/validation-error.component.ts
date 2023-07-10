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
    required: { message: 'Το πεδίο είναι υποχρεωτικό', priority: 0 },
    pattern: { message: 'Το πεδίο δεν είναι έγκυρο', priority: 2 },
    min: { message: 'Η τιμή που δώσατε είναι πολύ μικρή', priority: 1 },
    max: { message: 'Η τιμή που δώσατε είναι πολύ μεγάλη', priority: 1 },
    minlength: { message: 'Το πλήθος των χαρακτήρων είναι πολυ μικρό', priority: 1 },
    maxlength: { message: 'Το πλήθος των χαρακτήρων είναι πολύ μεγάλο', priority: 1 },
    futureDate: { message: 'Η ημερομηνία δεν μπορεί να είναι μεταγενέστερη της σημερινής', priority: 1 },
    pastDate: { message: 'Η ημερομηνία δεν μπορεί να είναι σημερινή ή προγενέστερη.', priority: 1 },
    pastExpiryDate: { message: 'Η ημερομηνία λήξης δεν μπορεί να είναι σημερινή ή προγενέστερη.', priority: 1 },
    minDate: { message: 'Μη έγκυρη ημερομηνία', priority: 1 },
    maxDate: { message: 'Μη έγκυρη ημερομηνία', priority: 1 },
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
        if (this._validationMessages[key as keyof typeof this._validationMessages].priority < this.maxPriority) {
          this.maxPriority = this._validationMessages[key as keyof typeof this._validationMessages].priority;
        }
      });

      return keys;
    }
  }

  public checkForError(key: any): boolean {
    return (
      this._validationMessages[key as keyof typeof this._validationMessages].priority === this.maxPriority ||
      !this._validationMessages[key as keyof typeof this._validationMessages].priority
    );
    // this._validationMessages[key]?.alwaysShow === true
  }

  public getErrorMessage(key: any): string {
    return this._validationMessages[key as keyof typeof this._validationMessages].message;
  }
}
