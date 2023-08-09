import { UtilService } from '../../shared/util/utils.service';
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

  public gameViewType = 'list';
  public gameViewTypeKey = 'gameViewType';

  public userGames = false;
  public fieldPrefix: string;
  public isGridFiltered = false;

  filterForm: FormGroup;

  public platformList: ICategory[] = [];
  public developerList: ICategory[] = [];
  public publisherList: ICategory[] = [];
  public categoryList: ICategory[] = [];

  public filteredPlatforms: Observable<ICategory[]> | undefined;
  public filteredDevelopers: Observable<ICategory[]> | undefined;
  public filteredPublishers: Observable<ICategory[]> | undefined;
  public filteredCategories: Observable<ICategory[]> | undefined;

  public takeOptions = [5, 10, 20];
  public takeKey = 'take';
  public state = {
    skip: 0,
    take: this.takeOptions[1],
    filter: { logic: 'and', filters: [] as any[] },
    sort: [{ field: 'title', dir: 'asc' }],
  };

  public sortKey = 'sort';
  public sortKeyValue = 'sortValue';
  public sortBy: {};
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

  constructor(
    public gameService: GameService,
    public userGameService: UserGameService,
    public platformService: PlatformService,
    public developerService: DeveloperService,
    public publisherService: PublisherService,
    public categoryService: CategoryService,
    public route: ActivatedRoute,
    private fb: FormBuilder,
    public utilService: UtilService
  ) {}

  ngOnInit(): void {
    this.filterForm = this.fb.group({
      title: [null],
      miniTitle: [null],
      platform: [null],
      developer: [null],
      publisher: [null],
      category: [null],
      price: [null],
      favourite: [null],
      sensitive: [null],
      sortBy: [this.sortByFilters[0]],
    });
    this.userGames = this.route.snapshot.data['userGames'];
    this.fieldPrefix = this.userGames ? 'game.gameDetails' : 'gameDetails';

    const previousGameViewType = sessionStorage.getItem(this.gameViewTypeKey);
    if (previousGameViewType != null) {
      this.gameViewType = previousGameViewType;
    }
    const previousPageLength = sessionStorage.getItem(this.takeKey);
    if (previousPageLength != null) {
      this.state.take = Number(previousPageLength);
    }
    const previousSort = sessionStorage.getItem(this.sortKey);
    if (previousSort != null) {
      const sort = JSON.parse(previousSort);
      const index = this.sortByFilters.findIndex(element => element.name === sort.name);
      this.filterForm.get('sortBy')?.setValue(this.sortByFilters[index]);

      this.handleSort(sort);
    }

    this.fetchGameData();
    this.fetchFilterData();
  }

  handlePageEvent(e: PageEvent): void {
    this.state.take = e.pageSize;
    sessionStorage.setItem(this.takeKey, this.state.take.toString());

    this.state.skip = e.pageIndex;
    this.fetchGameData();
  }

  public fetchFilterData(): void {
    this.platformService.getAllUsed().subscribe((result: EntityArrayResponseType) => {
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
    this.categoryService.getAllUsed().subscribe((result: EntityArrayResponseType) => {
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
      this.isGridFiltered = false;
    } else {
      if (this.state.filter.filters.length !== 0) {
        this.state.filter.filters = [];
        this.state.skip = 0;
        this.fetchGameData();
      }
    }
  }

  public filterSearch(): void {
    const title = this.filterForm.get('miniTitle')?.value;
    const developer = this.filterForm.get('developer')?.value;
    const platform = this.filterForm.get('platform')?.value;
    const publisher = this.filterForm.get('publisher')?.value;
    const category = this.filterForm.get('category')?.value;
    const favourite = this.filterForm.get('favourite')?.value;
    const sensitive = this.filterForm.get('sensitive')?.value;
    const sortBy = this.filterForm.get('sortBy')?.value;

    this.state.filter.filters = [];

    if (title != null && title !== '') {
      const newFilter = { field: 'title', operator: 'like', value: title };
      this.state.filter.filters.push(newFilter);
    }
    if (developer != null && developer !== '') {
      const description = developer.description != null ? developer.description : null;
      if (description != null) {
        const newFilter = { field: this.fieldPrefix + '.developers.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (platform != null && platform !== '') {
      const description = platform.description != null ? platform.description : null;
      if (description != null) {
        const newFilter = { field: this.fieldPrefix + '.platforms.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (publisher != null && publisher !== '') {
      const description = publisher.description != null ? publisher.description : null;
      if (description != null) {
        const newFilter = { field: this.fieldPrefix + '.publishers.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (category != null && category !== '') {
      const description = category.description != null ? category.description : null;
      if (description != null) {
        const newFilter = { field: this.fieldPrefix + '.categories.description', operator: 'like', value: description };
        this.state.filter.filters.push(newFilter);
      }
    }
    if (this.userGames && favourite) {
      const newFilter = { field: 'favourite', operator: 'eqbool', value: favourite };
      this.state.filter.filters.push(newFilter);
    }
    if (sensitive) {
      const newFilter = { field: 'gameDetails.notes', operator: 'isnotnull' };
      this.state.filter.filters.push(newFilter);
    }
    if (this.utilService.isNotNil(sortBy)) {
      const newSort = { field: sortBy.field, dir: sortBy.dir };

      this.handleSort(newSort);
      sessionStorage.setItem(this.sortKey, JSON.stringify(sortBy));
    }

    if (this.state.filter.filters.length === 0) {
      this.clearFormFields();
      this.isGridFiltered = false;
    } else {
      this.isGridFiltered = true;
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
          this.utilService.scrollToTop();
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
          this.utilService.scrollToTop();
        },
        () => {
          this.isLoading = false;
        }
      );
    }
  }

  public clearData(): void {
    this.clearFormFields();
    this.filterSearch();
  }

  public clearFormFields(): void {
    this.filterForm.get('miniTitle')?.setValue('');
    this.filterForm.get('platform')?.setValue('');
    this.filterForm.get('developer')?.setValue('');
    this.filterForm.get('publisher')?.setValue('');
    this.filterForm.get('category')?.setValue('');
    this.filterForm.get('favourite')?.setValue('');
    this.filterForm.get('sensitive')?.setValue('');
  }

  public setGameView(type: string): void {
    this.gameViewType = type;
    sessionStorage.setItem(this.gameViewTypeKey, type);
  }

  public handleSort(sort: any): void {
    let field = sort.field;
    field = field.replace('gameDetails', this.fieldPrefix);

    this.state.sort = [];
    this.state.sort.push({ field, dir: sort.dir });
  }
}
