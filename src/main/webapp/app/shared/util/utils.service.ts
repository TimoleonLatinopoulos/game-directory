import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { SnackBarAlertComponent } from '../components/snack-bar-alert/snack-bar-alert.component';

@Injectable({
  providedIn: 'root',
})
export class UtilService {
  public takeOptions = [5, 10, 20];
  public sortByFilters: any[] = [
    { name: 'Title (A to Z)', field: 'title', dir: 'asc' },
    { name: 'Title (Z to A)', field: 'title', dir: 'desc' },
    { name: 'Price (cheapest first)', field: 'gameDetails.price', dir: 'asc' },
    { name: 'Price (most expensive first)', field: 'gameDetails.price', dir: 'desc' },
    { name: 'Score (high to low)', field: 'gameDetails.metacriticScore', dir: 'desc' },
    { name: 'Score (low to high)', field: 'gameDetails.metacriticScore', dir: 'asc' },
    { name: 'Date Released (older to newer)', field: 'gameDetails.releaseDate', dir: 'asc' },
    { name: 'Date Released (newer to older)', field: 'gameDetails.releaseDate', dir: 'desc' },
  ];

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
