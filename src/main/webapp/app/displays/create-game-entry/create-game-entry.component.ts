/* eslint-disable @typescript-eslint/no-unsafe-return */
/* eslint-disable @typescript-eslint/no-unnecessary-condition */
import { EntityArrayResponseType, PlatformService } from 'app/entities/platform/service/platform.service';
import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Validators, FormGroup, FormBuilder, FormControl } from '@angular/forms';
import { DeveloperService } from 'app/entities/developer/service/developer.service';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { CategoryService } from 'app/entities/category/service/category.service';
import { IPlatform } from 'app/entities/platform/platform.model';
import { IDeveloper } from 'app/entities/developer/developer.model';
import { ICategory } from 'app/entities/category/category.model';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { SteamApiService } from 'app/entities/game-details/service/steam-api.service';
import { ISteamGame } from 'app/entities/game-details/steam-game-details.model';
import { greekDateToEng, getDateWithTimeOffset } from 'app/shared/util/date-utils';
import { NgbDateAdapter, NgbDatepicker } from '@ng-bootstrap/ng-bootstrap';
import { MatChipInputEvent } from '@angular/material/chips';
import { MatAutocomplete, MatAutocompleteSelectedEvent } from '@angular/material/autocomplete';
import { Observable } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { COMMA, ENTER } from '@angular/cdk/keycodes';

@Component({
  selector: 'jhi-create-game-entry',
  templateUrl: './create-game-entry.component.html',
  styleUrls: ['./create-game-entry.component.scss'],
})
export class CreateGameEntryComponent implements OnInit {
  separatorKeysCodes: number[] = [ENTER, COMMA];

  gameDetailsForm: FormGroup;
  pegiRatingList: string[] = ['Three', 'Seven', 'Twelve', 'Sixteen', 'Eighteen'];

  public platformList: IPlatform[] | null = [];
  public filteredPlatformList: Observable<IPlatform[] | undefined> | undefined;
  public platforms: IPlatform[] | null = [];

  public developerList: IDeveloper[] | null = [];
  public publisherList: IPublisher[] | null = [];
  public categoryList: ICategory[] | null = [];
  public steamGame: ISteamGame | null = null;

  public appid: number | undefined;

  @ViewChild('platformInput', { static: false }) platformInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto', { static: false }) matAutocomplete: MatAutocomplete;

  constructor(
    private fb: FormBuilder,
    private platformService: PlatformService,
    private developerService: DeveloperService,
    private publisherService: PublisherService,
    private categoryService: CategoryService,
    private steamApiService: SteamApiService
  ) {
    this.gameDetailsForm = this.fb.group({
      id: [null],
      title: ['', Validators.required],
      gameDetails: this.fb.group({
        id: [null],
        releaseDate: [{ day: 20, month: 4, year: 1969 }, [Validators.required]], // Validators.pattern(DATE_FORMAT)
        pegiRating: [null],
        metacriticScore: [null, [Validators.min(0), Validators.max(100)]],
        imageUrl: ['', Validators.required],
        thumbnailUrl: ['', Validators.required],
        price: [null, Validators.required],
        description: ['', Validators.required],
        snippet: ['', Validators.required],
        notes: [''],
        steamApiid: [null, Validators.required],
        platforms: [[]],
        developers: [[]],
        publishers: [[]],
        categories: [[]],
      }),
    });

    this.filteredPlatformList = this.gameDetailsForm
      .get('gameDetails')
      ?.get('platforms')
      ?.valueChanges.pipe(
        startWith(null),
        map((platform: IPlatform | null) => (platform ? this._filter(platform) : this.platformList?.slice()))
      );
  }

  ngOnInit(): void {
    this.fetchData();
  }

  public fetchData(): void {
    this.platformService.getAll().subscribe((result: EntityArrayResponseType) => {
      this.platformList = result.body;
    });
    this.developerService.getAll().subscribe((result: EntityArrayResponseType) => {
      this.developerList = result.body;
    });
    this.publisherService.getAll().subscribe((result: EntityArrayResponseType) => {
      this.publisherList = result.body;
    });
    this.categoryService.getAll().subscribe((result: EntityArrayResponseType) => {
      this.categoryList = result.body;
    });
  }

  public save(): void {
    return;
  }

  public fillFormFromSteam(): void {
    const apiid = this.gameDetailsForm.get('gameDetails')?.get('steamApiid')?.value;

    if (apiid != null) {
      this.steamApiService.find(apiid).subscribe((result: any) => {
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
          this.gameDetailsForm.get('gameDetails')?.get('steamApiid')?.setValue(this.steamGame?.data.steam_appid);
        }
      });
    }
  }

  public getPegiRating(requiredAge: any): string {
    const age = Number(requiredAge);
    switch (age) {
      case 3:
        return this.pegiRatingList[0];
      case 7:
        return this.pegiRatingList[1];
      case 12:
        return this.pegiRatingList[2];
      case 16:
        return this.pegiRatingList[3];
      case 18:
        return this.pegiRatingList[4];
      default:
        return 'Zero';
    }
  }

  add(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();

    // Add our platform
    if (value) {
      const platformResult = this.platformList?.filter(obj => obj.description === value);
      if (platformResult !== undefined) {
        this.platforms?.push(platformResult[0]);

        // Clear the input value
        event.chipInput.clear();
        this.gameDetailsForm.get('gameDetails')?.get('platforms')?.setValue(null);
      }
    }
  }

  remove(platform: IPlatform): void {
    const index = this.platforms?.indexOf(platform);

    if (index !== undefined && index >= 0) {
      this.platforms?.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    const platformResult = this.platformList?.filter(obj => obj.description === event.option.viewValue);
    if (platformResult !== undefined) {
      this.platforms?.push(platformResult[0]);
      this.platformInput.nativeElement.value = '';
      this.gameDetailsForm.get('gameDetails')?.get('platforms')?.setValue(null);
    }
  }

  private _filter(value: any): IPlatform[] | undefined {
    if (value !== undefined) {
      let filterValue = '';
      if (typeof value === 'string') {
        filterValue = value.toLowerCase();
      } else {
        filterValue = value.description?.toLowerCase();
      }

      if (filterValue != null) {
        return this.platformList?.filter(platform => platform.description?.toLowerCase().includes(filterValue));
      }
    }
    return undefined;
  }
}
