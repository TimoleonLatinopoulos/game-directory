import { Component, Input, ElementRef, ViewChild, EventEmitter, Output, OnChanges, SimpleChanges } from '@angular/core';
import { Observable, map, startWith } from 'rxjs';
import { COMMA, ENTER } from '@angular/cdk/keycodes';
import { FormBuilder, FormControl } from '@angular/forms';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatAutocomplete, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Category, ICategory } from 'app/entities/category/category.model';

@Component({
  selector: 'jhi-chip-grid-input',
  templateUrl: './chip-grid-input.component.html',
  styleUrls: ['./chip-grid-input.component.scss'],
})
export class ChipGridInputComponent implements OnChanges {
  @ViewChild('categoryInput', { static: false }) categoryInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', { static: false }) matAutocomplete: MatAutocomplete;

  _categoryList: ICategory[];
  @Input()
  set categoryList(value: ICategory[]) {
    this._categoryList = value;
    if (this._categoryList !== undefined) {
      this.filteredCategoryList = this.categoryForm.valueChanges.pipe(
        startWith(null),
        map((category: ICategory | null) => this._filter(category))
      );
    }
  }
  @Input() placeholder: string;
  @Input() canAdd = false;
  @Input() recievedCategoryList: ICategory[];
  @Output() valueEmitted = new EventEmitter<ICategory[]>();
  @Output() filterValueEmitted = new EventEmitter<string>();

  separatorKeysCodes: number[] = [ENTER, COMMA];
  public filteredCategoryList: Observable<ICategory[]>;
  public categories: ICategory[] | null = [];
  public categoryForm = new FormControl(null);

  constructor(private fb: FormBuilder) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.recievedCategoryList && !changes.recievedCategoryList.firstChange) {
      const listLength = changes.recievedCategoryList.currentValue.length;
      this.empty();
      for (let i = 0; i < listLength; i++) {
        this.addValue(changes.recievedCategoryList.currentValue[i]);
      }
    }
    return;
  }

  emitValue(): void {
    if (this.categories != null) {
      this.valueEmitted.emit(this.categories);
    }
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our category
    if (value) {
      const categoryResult = this._categoryList.filter(
        obj => obj.description === value || obj.description?.toLowerCase() === value.toLowerCase()
      );
      if (categoryResult.length === 0) {
        if (this.canAdd) {
          categoryResult.push(new Category(0, value, null));
          this.push(categoryResult[0]);
        }
      } else {
        this.push(categoryResult[0]);
      }

      // Clear the input value
      event.chipInput.clear();
      this.categoryForm.setValue(null);
      this.emitValue();
    }
  }

  push(value: any): void {
    const categoryResult = this.categories?.filter(obj => obj.description === value.description);
    if (categoryResult !== undefined && categoryResult.length === 0) {
      this.categories?.push(value);
    }
  }

  remove(category: ICategory): void {
    const index = this.categories?.indexOf(category);

    if (index !== undefined && index >= 0) {
      this.categories?.splice(index, 1);
      this.emitValue();
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const categoryResult = this._categoryList.filter(obj => obj.description === event.option.viewValue);
    this.push(categoryResult[0]);

    this.categoryInput.nativeElement.value = '';
    this.categoryForm.setValue(null);
    this.emitValue();
  }

  addValue(value: ICategory): void {
    this.push(value);

    this.categoryInput.nativeElement.value = '';
    this.categoryForm.setValue(null);
    this.emitValue();
  }

  empty(): void {
    this.categories = [];

    this.categoryInput.nativeElement.value = '';
    this.categoryForm.setValue(null);
    this.emitValue();
  }

  private _filter(value: any): ICategory[] {
    if (value || value === '') {
      if (value !== undefined) {
        let filterValue = '';
        if (typeof value === 'string') {
          filterValue = value.toLowerCase();
          this.filterValueEmitted.emit(filterValue);
        } else {
          filterValue = value.description?.toLowerCase();
          this.filterValueEmitted.emit('');
        }

        return this._categoryList.filter(category => category.description?.toLowerCase().includes(filterValue));
      }
      return [];
    } else {
      return this._categoryList.slice();
    }
  }
}
