import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { PageEvent } from '@angular/material/paginator';
import { ActivatedRoute } from '@angular/router';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';
import { DeveloperService } from 'app/entities/developer/service/developer.service';
import { ISearchType } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';
import { EntityArrayResponseType, PlatformService } from 'app/entities/platform/service/platform.service';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { UserGameService } from 'app/entities/user-game/service/user-game.service';
import { Observable, map, startWith } from 'rxjs';

@Component({
  selector: 'jhi-game-list',
  templateUrl: './game-list.component.html',
  styleUrls: ['./game-list.component.scss'],
})
export class GameListComponent implements OnInit {
  public isLoading = false;
  public gameListDataResult: ISearchType;

  public userGames = false;
  filterForm: FormGroup;

  public platformList: ICategory[] = [];
  public developerList: ICategory[] = [];
  public publisherList: ICategory[] = [];
  public categoryList: ICategory[] = [];

  public filteredPlatforms: Observable<ICategory[]> | undefined;
  public filteredDevelopers: Observable<ICategory[]> | undefined;
  public filteredPublishers: Observable<ICategory[]> | undefined;
  public filteredCategories: Observable<ICategory[]> | undefined;

  public state = {
    skip: 0,
    take: 10,
    filter: { logic: 'and', filters: [] as any[] },
    sort: [{ field: 'title', dir: 'asc' }],
  };

  constructor(
    public gameService: GameService,
    public userGameService: UserGameService,
    public platformService: PlatformService,
    public developerService: DeveloperService,
    public publisherService: PublisherService,
    public categoryService: CategoryService,
    public route: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.filterForm = this.fb.group({
      title: [null],
      platform: [null],
      developer: [null],
      publisher: [null],
      category: [null],
      price: [null],
      favourite: [null],
    });
    this.userGames = this.route.snapshot.data['userGames'];
    this.fetchGameData();
    this.fetchFilterData();
  }

  handlePageEvent(e: PageEvent): void {
    this.state.take = e.pageSize;
    this.state.skip = e.pageIndex;
    this.fetchGameData();
  }

  public fetchFilterData(): void {
    this.platformService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.platformList = result.body;
        this.filteredPlatforms = this.filterForm.get('platform')?.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '', this.platformList))
        );
      }
    });
    this.developerService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.developerList = result.body;
        this.filteredDevelopers = this.filterForm.get('developer')?.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '', this.developerList))
        );
      }
    });
    this.publisherService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.publisherList = result.body;
        this.filteredPublishers = this.filterForm.get('publisher')?.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '', this.publisherList))
        );
      }
    });
    this.categoryService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.categoryList = result.body;
        this.filteredCategories = this.filterForm.get('category')?.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value || '', this.categoryList))
        );
        this.filterForm.updateValueAndValidity();
      }
    });
  }

  public _filter(name: string | ICategory, list: ICategory[]): ICategory[] {
    let filterValue: string;
    if (typeof name === 'string') {
      filterValue = name.toLowerCase();
    } else {
      if (name.description != null) {
        filterValue = name.description.toLowerCase();
      }
    }

    return list.filter(option => option.description?.toLowerCase().includes(filterValue));
  }

  public displayFn(category: ICategory | null): string {
    return category?.description ? category.description : '';
  }

  public filter(): void {
    const value = this.filterForm.get('title')?.value;
    if (value && value.length >= 3) {
      const newFilter = { field: 'title', operator: 'like', value };
      this.state.filter.filters = [];
      this.state.filter.filters.push(newFilter);
      this.state.skip = 0;
      this.fetchGameData();
      this.clearFormFields();
    } else {
      if (this.state.filter.filters.length !== 0) {
        this.state.filter.filters = [];
        this.state.skip = 0;
        this.fetchGameData();
      }
    }
  }

  public filterSearch(): void {
    const developer = this.filterForm.get('developer')?.value;
    const platform = this.filterForm.get('platform')?.value;
    const publisher = this.filterForm.get('publisher')?.value;
    const category = this.filterForm.get('category')?.value;
    const favourite = this.filterForm.get('favourite')?.value;

    this.state.filter.filters = [];

    const fieldPrefix = this.userGames ? 'game.gameDetails' : 'gameDetails';

    if (developer != null && developer !== '') {
      const description = developer.description != null ? developer.description : null;
      if (description != null) {
        const newFilter = { field: fieldPrefix + '.developers.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (platform != null && platform !== '') {
      const description = platform.description != null ? platform.description : null;
      if (description != null) {
        const newFilter = { field: fieldPrefix + '.platforms.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (publisher != null && publisher !== '') {
      const description = publisher.description != null ? publisher.description : null;
      if (description != null) {
        const newFilter = { field: fieldPrefix + '.publishers.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (category != null && category !== '') {
      const description = category.description != null ? category.description : null;
      if (description != null) {
        const newFilter = { field: fieldPrefix + '.categories.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (this.userGames && favourite) {
      const newFilter = { field: 'favourite', operator: 'eqbool', value: favourite };
      this.state.filter.filters.push(newFilter);
    }

    if (this.state.filter.filters.length === 0) {
      this.clearFormFields();
    }

    this.state.skip = 0;
    this.fetchGameData();
    this.filterForm.get('title')?.setValue('');
  }

  public fetchGameData(): void {
    this.isLoading = true;
    if (this.userGames) {
      this.userGameService.search(this.state).subscribe(
        (res: any) => {
          this.gameListDataResult = {
            data: res.data.content,
            total: res.data.totalEntries,
          };
          this.isLoading = false;
          window.scrollTo(0, 0);
        },
        () => {
          this.isLoading = false;
        }
      );
    } else {
      this.gameService.search(this.state).subscribe(
        (res: any) => {
          this.gameListDataResult = {
            data: res.data.content,
            total: res.data.totalEntries,
          };
          this.isLoading = false;
          window.scrollTo(0, 0);
        },
        () => {
          this.isLoading = false;
        }
      );
    }
  }

  public clearFormFields(): void {
    this.filterForm.get('platform')?.setValue('');
    this.filterForm.get('developer')?.setValue('');
    this.filterForm.get('publisher')?.setValue('');
    this.filterForm.get('category')?.setValue('');
    this.filterForm.get('favourite')?.setValue('');
  }
}
