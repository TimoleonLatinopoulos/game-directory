import { Pipe, PipeTransform } from '@angular/core';
import { ICategory } from 'app/entities/category/category.model';

@Pipe({
  name: 'separateCategories',
})
export class SeparateCategoriesPipe implements PipeTransform {
  transform(value: ICategory[] | null | undefined): string {
    if (value) {
      return value.map(item => item.description).join(', ');
    }
    return '';
  }
}
