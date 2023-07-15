import { Component, Inject, inject } from '@angular/core';
import { MAT_SNACK_BAR_DATA, MatSnackBarRef } from '@angular/material/snack-bar';

@Component({
  selector: 'jhi-snack-bar-alert',
  templateUrl: './snack-bar-alert.component.html',
  styleUrls: ['./snack-bar-alert.component.scss'],
})
export class SnackBarAlertComponent {
  public snackBarRef = inject(MatSnackBarRef);

  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: any) {}
}
