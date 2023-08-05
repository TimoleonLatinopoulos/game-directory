import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarAlertComponent } from '../components/snack-bar-alert/snack-bar-alert.component';

@Injectable({
  providedIn: 'root',
})
export class UtilService {
  constructor(private snackBar: MatSnackBar) {}

  public isNil(value: any): boolean {
    return value == null || value === undefined;
  }

  public isNotNil(value: any): boolean {
    return value != null && value !== undefined;
  }

  public scrollToTop(): void {
    window.scrollTo(0, 0);
  }

  public openSnackBar(message: string, type = 'error', time = 5000): void {
    this.snackBar.openFromComponent(SnackBarAlertComponent, {
      duration: time,
      data: message,
      panelClass: ['snackbar', type === 'error' ? 'error-snackbar' : 'success-snackbar', 'snackbar'],
    });
  }
}
