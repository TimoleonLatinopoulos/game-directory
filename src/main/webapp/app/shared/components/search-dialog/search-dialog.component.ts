import { FormBuilder, FormGroup } from '@angular/forms';
import { Component, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { ICategory } from 'app/entities/category/category.model';
import { UtilService } from 'app/shared/util/utils.service';
import { EntityArrayResponseType, PlatformService } from 'app/entities/platform/service/platform.service';
import { DeveloperService } from 'app/entities/developer/service/developer.service';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { CategoryService } from 'app/entities/category/service/category.service';
import { switchMap } from 'rxjs';
import { GameService } from 'app/entities/game/service/game.service';
import { ISearchType } from 'app/entities/game/game.model';
import { PageEvent } from '@angular/material/paginator';
import { UserGameService } from 'app/entities/user-game/service/user-game.service';

@Component({
  selector: 'jhi-search-dialog',
  templateUrl: './search-dialog.component.html',
  styleUrls: ['./search-dialog.component.scss'],
})
export class SearchDialogComponent implements OnInit {
  public sortBy: {};
  public sortByFilters: any[] = this.utilService.sortByFilters;
  public filterForm: FormGroup;

  public platformList: ICategory[] = [];
  public developerList: ICategory[] = [];
  public publisherList: ICategory[] = [];
  public categoryList: ICategory[] = [];

  public gameListDataResult: ISearchType | undefined;
  public state = {
    skip: 0,
    take: this.utilService.takeOptions[1],
    filter: { logic: 'and', filters: [] as any[] },
    sort: [{ field: 'title', dir: 'asc' }],
  };

  public isLoading = false;

  constructor(
    public dialogRef: MatDialogRef<any>,
    public utilService: UtilService,
    public fb: FormBuilder,
    public platformService: PlatformService,
    public developerService: DeveloperService,
    public publisherService: PublisherService,
    public categoryService: CategoryService,
    public gameService: GameService,
    public userGameService: UserGameService
  ) {}

  ngOnInit(): void {
    // this.dialogRef.updateSize('60%', '60%');

    this.filterForm = this.fb.group({
      title: [null],
      platform: [null],
      developer: [null],
      publisher: [null],
      category: [null],
      sortBy: [this.sortByFilters[0]],
    });

    this.fetchFilterData();
  }

  public displayFn(category: ICategory | null): string {
    return category?.description ? category.description : '';
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  public fetchFilterData(): void {
    this.filterForm
      .get('platform')
      ?.valueChanges.pipe(switchMap((value: string) => this.platformService.getUsedResults(value)))
      .subscribe((result: EntityArrayResponseType) => {
        if (result.body != null) {
          this.platformList = result.body;
        }
      });

    this.filterForm
      .get('developer')
      ?.valueChanges.pipe(switchMap((value: string) => this.developerService.getResults(value)))
      .subscribe((result: EntityArrayResponseType) => {
        if (result.body != null) {
          this.developerList = result.body;
        }
      });

    this.filterForm
      .get('publisher')
      ?.valueChanges.pipe(switchMap((value: string) => this.publisherService.getResults(value)))
      .subscribe((result: EntityArrayResponseType) => {
        if (result.body != null) {
          this.publisherList = result.body;
        }
      });

    this.filterForm
      .get('category')
      ?.valueChanges.pipe(switchMap((value: string) => this.categoryService.getUsedResults(value)))
      .subscribe((result: EntityArrayResponseType) => {
        if (result.body != null) {
          this.categoryList = result.body;
        }
      });

    this.filterForm.get('platform')?.setValue('');
    this.filterForm.get('developer')?.setValue('');
    this.filterForm.get('publisher')?.setValue('');
    this.filterForm.get('category')?.setValue('');
  }

  handlePageEvent(e: PageEvent): void {
    this.state.take = e.pageSize;
    this.state.skip = e.pageIndex;
    this.fetchGameData();
  }

  public fetchGameData(): void {
    this.isLoading = true;
    this.gameService.searchUser(this.state).subscribe(
      (res: any) => {
        this.gameListDataResult = {
          data: res.data.content,
          total: res.data.totalEntries,
        };
        this.isLoading = false;
        this.utilService.scrollToTop();
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  public filterSearch(): void {
    const title = this.filterForm.get('title')?.value;
    const developer = this.filterForm.get('developer')?.value;
    const platform = this.filterForm.get('platform')?.value;
    const publisher = this.filterForm.get('publisher')?.value;
    const category = this.filterForm.get('category')?.value;
    const sortBy = this.filterForm.get('sortBy')?.value;

    this.state.filter.filters = [];

    if (title != null && title !== '') {
      const newFilter = { field: 'title', operator: 'like', value: title };
      this.state.filter.filters.push(newFilter);
    }
    if (developer != null && developer !== '') {
      const description = developer.description != null ? developer.description : null;
      if (description != null) {
        const newFilter = { field: 'gameDetails.developers.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (platform != null && platform !== '') {
      const description = platform.description != null ? platform.description : null;
      if (description != null) {
        const newFilter = { field: 'gameDetails.platforms.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (publisher != null && publisher !== '') {
      const description = publisher.description != null ? publisher.description : null;
      if (description != null) {
        const newFilter = { field: 'gameDetails.publishers.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (category != null && category !== '') {
      const description = category.description != null ? category.description : null;
      if (description != null) {
        const newFilter = { field: 'gameDetails.categories.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (this.utilService.isNotNil(sortBy)) {
      this.state.sort = [];
      this.state.sort.push({ field: sortBy.field, dir: sortBy.dir });
    }

    if (this.state.filter.filters.length === 0) {
      this.clearFormFields();
    }

    this.state.skip = 0;
    this.fetchGameData();
  }

  public clearData(): void {
    this.clearFormFields();
    this.gameListDataResult = undefined;
  }

  public clearFormFields(): void {
    this.filterForm.get('title')?.setValue('');
    this.filterForm.get('platform')?.setValue('');
    this.filterForm.get('developer')?.setValue('');
    this.filterForm.get('publisher')?.setValue('');
    this.filterForm.get('category')?.setValue('');
  }

  public addToUserGames(game: any): void {
    this.userGameService.create(game.id).subscribe(
      () => {
        this.utilService.openSnackBar('The game has been added to your games list!', 'success');
        this.fetchGameData();
      },
      (error: any) => {
        this.utilService.openSnackBar(error.error.detail, 'error');
      }
    );
  }
}
