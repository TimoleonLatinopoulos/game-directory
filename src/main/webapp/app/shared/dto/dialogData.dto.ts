import { FormGroup } from '@angular/forms';
import { ICategory } from 'app/entities/category/category.model';
import { Observable } from 'rxjs';

export interface DialogData {
  dialogTitle: string;
  dialogMessage: string;
  dialogYesButton: string;
  dialogNoButton: string;
}

export interface SearchDialogData {
  filteredPlatforms: Observable<ICategory[]> | undefined;
  filteredDevelopers: Observable<ICategory[]> | undefined;
  filteredPublishers: Observable<ICategory[]> | undefined;
  filteredCategories: Observable<ICategory[]> | undefined;
  filterForm: FormGroup;
}
