import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from 'app/shared/dto/dialogData.dto';

@Component({
  selector: 'jhi-dialog',
  templateUrl: './choice-dialog.component.html',
  styleUrls: ['./choice-dialog.component.scss'],
})
export class ChoiceDialogComponent {
  public result: boolean;

  constructor(public dialogRef: MatDialogRef<any>, @Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  onNoClick(): void {
    this.dialogRef.close();
  }
}
