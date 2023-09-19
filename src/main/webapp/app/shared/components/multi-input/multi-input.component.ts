import { Component, Input, Output, EventEmitter } from '@angular/core';

@Component({
  selector: 'jhi-multi-input',
  templateUrl: './multi-input.component.html',
  styleUrls: ['./multi-input.component.scss'],
})
export class MultiInputComponent {
  @Input() options: string[] | undefined;
  @Input() label: string | undefined;
  @Output() selectionChange = new EventEmitter<string[]>();

  selectedOptions: string[] = [];

  emitSelectedOptions(): void {
    this.selectionChange.emit(this.selectedOptions);
  }
}
