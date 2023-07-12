/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unnecessary-condition */
import { EntityArrayResponseType, PlatformService } from 'app/entities/platform/service/platform.service';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Validators, FormGroup, FormBuilder } from '@angular/forms';
import { DeveloperService } from 'app/entities/developer/service/developer.service';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { CategoryService } from 'app/entities/category/service/category.service';
import { Category, ICategory } from 'app/entities/category/category.model';
import { SteamApiService } from 'app/entities/game-details/service/steam-api.service';
import { ISteamGame } from 'app/entities/game-details/steam-game-details.model';
import { greekDateToEng, getDateWithTimeOffset } from 'app/shared/util/date-utils';
import { MatAutocomplete } from '@angular/material/autocomplete';
import { GameService } from 'app/entities/game/service/game.service';

@Component({
  selector: 'jhi-create-game-entry',
  templateUrl: './create-game-entry.component.html',
  styleUrls: ['./create-game-entry.component.scss'],
})
export class CreateGameEntryComponent implements OnInit {
  platformPlaceholder = 'New Platform...';
  developerPlaceholder = 'New Developer...';
  publisherPlaceholder = 'New Publisher...';
  categoryPlaceholder = 'New Category...';

  gameDetailsForm: FormGroup;
  pegiRatingList: string[] = ['Zero', 'Three', 'Seven', 'Twelve', 'Sixteen', 'Eighteen'];

  public platformList: ICategory[] = [];
  public developerList: ICategory[] = [];
  public publisherList: ICategory[] = [];
  public categoryList: ICategory[] = [];
  public steamGame: ISteamGame | null = null;

  public steamPlatformList: ICategory[] = [];
  public steamPublisherList: ICategory[] = [];
  public steamDeveloperList: ICategory[] = [];
  public steamCategoryList: ICategory[] = [];

  public appid: number | undefined;
  public showSpinner = false;

  @ViewChild('platformInput', { static: false }) platformInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', { static: false }) matAutocomplete: MatAutocomplete;

  constructor(
    private fb: FormBuilder,
    private platformService: PlatformService,
    private developerService: DeveloperService,
    private publisherService: PublisherService,
    private categoryService: CategoryService,
    private steamApiService: SteamApiService,
    private gameService: GameService
  ) {
    this.gameDetailsForm = this.fb.group({
      id: [null],
      title: ['', Validators.required],
      gameDetails: this.fb.group({
        id: [null],
        releaseDate: [{ day: 20, month: 4, year: 1969 }, [Validators.required]], // Validators.pattern(DATE_FORMAT)
        pegiRating: [null, Validators.required],
        metacriticScore: [null, [Validators.min(0), Validators.max(100)]],
        imageUrl: ['', Validators.required],
        thumbnailUrl: ['', Validators.required],
        price: [null, Validators.required],
        description: ['', Validators.required],
        snippet: ['', Validators.required],
        notes: [''],
        steamAppid: [null, Validators.required],
        platforms: [[]],
        developers: [[]],
        publishers: [[]],
        categories: [[]],
      }),
    });
  }

  ngOnInit(): void {
    this.fetchData();
  }

  public fetchData(): void {
    this.platformService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.platformList = result.body;
      }
    });
    this.developerService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.developerList = result.body;
      }
    });
    this.publisherService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.publisherList = result.body;
      }
    });
    this.categoryService.getAll().subscribe((result: EntityArrayResponseType) => {
      if (result.body != null) {
        this.categoryList = result.body;
      }
    });
  }

  public save(): void {
    this.gameService.create(this.gameDetailsForm.value).subscribe((response: any) => {
      this.gameDetailsForm.reset();
    });
  }

  public fillFormFromSteam(): void {
    const apiid = this.gameDetailsForm.get('gameDetails')?.get('steamAppid')?.value;

    if (apiid != null) {
      this.showSpinner = true;
      this.steamApiService.find(apiid).subscribe(
        (result: any) => {
          this.steamGame = result.body[Object.keys(result.body)[0]];
          if (this.steamGame?.success) {
            this.gameDetailsForm.get('title')?.setValue(this.steamGame?.data.name);

            const dateString = greekDateToEng(this.steamGame?.data.release_date.date);
            if (dateString !== undefined) {
              this.gameDetailsForm
                .get('gameDetails')
                ?.get('releaseDate')
                ?.setValue(getDateWithTimeOffset(dateString).toISOString().substring(0, 10));
            }

            const pegiRating = this.getPegiRating(this.steamGame?.data.required_age);
            this.gameDetailsForm.get('gameDetails')?.get('pegiRating')?.patchValue(pegiRating);

            this.gameDetailsForm.get('gameDetails')?.get('metacriticScore')?.setValue(this.steamGame?.data.metacritic?.score);
            this.gameDetailsForm.get('gameDetails')?.get('imageUrl')?.setValue(this.steamGame?.data.header_image);
            this.gameDetailsForm.get('gameDetails')?.get('thumbnailUrl')?.setValue(this.steamGame?.data.capsule_image);

            const price = this.steamGame?.data.price_overview.final;
            if (price != null) {
              this.gameDetailsForm
                .get('gameDetails')
                ?.get('price')
                ?.setValue(price / 100);
            }
            this.gameDetailsForm.get('gameDetails')?.get('description')?.setValue(this.steamGame?.data.about_the_game);
            this.gameDetailsForm.get('gameDetails')?.get('snippet')?.setValue(this.steamGame?.data.short_description);
            this.gameDetailsForm.get('gameDetails')?.get('notes')?.setValue(this.steamGame?.data.content_descriptors?.notes);
            this.gameDetailsForm.get('gameDetails')?.get('steamAppid')?.setValue(this.steamGame?.data.steam_appid);

            this.steamPlatformList = this.filterCategory(this.platformList, ['PC']);
            this.gameDetailsForm.get('gameDetails')?.get('platforms')?.setValue(this.steamPlatformList);

            this.steamPublisherList = this.filterCategoryNew(this.publisherList, this.steamGame?.data.publishers);
            this.gameDetailsForm.get('gameDetails')?.get('publishers')?.setValue(this.steamPublisherList);

            this.steamDeveloperList = this.filterCategoryNew(this.developerList, this.steamGame?.data.developers);
            this.gameDetailsForm.get('gameDetails')?.get('developers')?.setValue(this.steamDeveloperList);

            const stringCategories = this.steamGame?.data.genres.map(a => a.description);
            this.steamCategoryList = this.filterCategory(this.categoryList, stringCategories);
            this.gameDetailsForm.get('gameDetails')?.get('categories')?.setValue(this.steamCategoryList);
          }
          this.showSpinner = false;
        },
        () => {
          this.showSpinner = false;
        }
      );
    }
  }

  public getPegiRating(requiredAge: any): string {
    const age = Number(requiredAge);
    switch (age) {
      case 0:
        return this.pegiRatingList[0];
      case 3:
        return this.pegiRatingList[1];
      case 7:
        return this.pegiRatingList[2];
      case 12:
        return this.pegiRatingList[3];
      case 16:
        return this.pegiRatingList[4];
      case 18:
        return this.pegiRatingList[5];
      default:
        return this.pegiRatingList[0];
    }
  }

  public filterCategory(list: ICategory[], values: any[]): ICategory[] {
    const lowerCaseValues = values.map(item => item.toLowerCase());
    const result = list.filter(item =>
      lowerCaseValues.includes(item.description != null && item.description !== undefined ? item.description.toLowerCase() : '')
    );
    return result;
  }

  public filterCategoryNew(list: ICategory[], values: any[]): ICategory[] {
    const result = this.filterCategory(list, values);

    if (result.length < values.length) {
      const stringValues = list.map(a => a.description);
      const restOfValues = values.filter(item => !stringValues.includes(item != null && item !== undefined ? item.toLowerCase() : ''));

      for (let i = 0; i < restOfValues.length; i++) {
        result.push(new Category(0, restOfValues[i], null));
      }
    }
    return result;
  }

  handlePlatformValue(value: ICategory[]): void {
    this.gameDetailsForm.get('gameDetails')?.get('platforms')?.setValue(value);
  }

  handleDeveloperValue(value: ICategory[]): void {
    this.gameDetailsForm.get('gameDetails')?.get('developers')?.setValue(value);
  }

  handlePublisherValue(value: ICategory[]): void {
    this.gameDetailsForm.get('gameDetails')?.get('publishers')?.setValue(value);
  }

  handleCategoryValue(value: ICategory[]): void {
    this.gameDetailsForm.get('gameDetails')?.get('categories')?.setValue(value);
  }
}
