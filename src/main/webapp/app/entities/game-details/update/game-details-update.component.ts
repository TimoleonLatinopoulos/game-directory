import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { GameDetailsFormService, GameDetailsFormGroup } from './game-details-form.service';
import { IGameDetails } from '../game-details.model';
import { GameDetailsService } from '../service/game-details.service';
import { IPlatform } from 'app/entities/platform/platform.model';
import { PlatformService } from 'app/entities/platform/service/platform.service';
import { IDeveloper } from 'app/entities/developer/developer.model';
import { DeveloperService } from 'app/entities/developer/service/developer.service';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { PublisherService } from 'app/entities/publisher/service/publisher.service';
import { ICategory } from 'app/entities/category/category.model';
import { CategoryService } from 'app/entities/category/service/category.service';

@Component({
  standalone: true,
  selector: 'jhi-game-details-update',
  templateUrl: './game-details-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GameDetailsUpdateComponent implements OnInit {
  isSaving = false;
  gameDetails: IGameDetails | null = null;

  platformsSharedCollection: IPlatform[] = [];
  developersSharedCollection: IDeveloper[] = [];
  publishersSharedCollection: IPublisher[] = [];
  categoriesSharedCollection: ICategory[] = [];

  editForm: GameDetailsFormGroup = this.gameDetailsFormService.createGameDetailsFormGroup();

  constructor(
    protected gameDetailsService: GameDetailsService,
    protected gameDetailsFormService: GameDetailsFormService,
    protected platformService: PlatformService,
    protected developerService: DeveloperService,
    protected publisherService: PublisherService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePlatform = (o1: IPlatform | null, o2: IPlatform | null): boolean => this.platformService.comparePlatform(o1, o2);

  compareDeveloper = (o1: IDeveloper | null, o2: IDeveloper | null): boolean => this.developerService.compareDeveloper(o1, o2);

  comparePublisher = (o1: IPublisher | null, o2: IPublisher | null): boolean => this.publisherService.comparePublisher(o1, o2);

  compareCategory = (o1: ICategory | null, o2: ICategory | null): boolean => this.categoryService.compareCategory(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ gameDetails }) => {
      this.gameDetails = gameDetails;
      if (gameDetails) {
        this.updateForm(gameDetails);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const gameDetails = this.gameDetailsFormService.getGameDetails(this.editForm);
    if (gameDetails.id !== null) {
      this.subscribeToSaveResponse(this.gameDetailsService.update(gameDetails));
    } else {
      this.subscribeToSaveResponse(this.gameDetailsService.create(gameDetails));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGameDetails>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(gameDetails: IGameDetails): void {
    this.gameDetails = gameDetails;
    this.gameDetailsFormService.resetForm(this.editForm, gameDetails);

    this.platformsSharedCollection = this.platformService.addPlatformToCollectionIfMissing<IPlatform>(
      this.platformsSharedCollection,
      ...(gameDetails.platforms ?? [])
    );
    this.developersSharedCollection = this.developerService.addDeveloperToCollectionIfMissing<IDeveloper>(
      this.developersSharedCollection,
      ...(gameDetails.developers ?? [])
    );
    this.publishersSharedCollection = this.publisherService.addPublisherToCollectionIfMissing<IPublisher>(
      this.publishersSharedCollection,
      ...(gameDetails.publishers ?? [])
    );
    this.categoriesSharedCollection = this.categoryService.addCategoryToCollectionIfMissing<ICategory>(
      this.categoriesSharedCollection,
      ...(gameDetails.categories ?? [])
    );
  }

  protected loadRelationshipsOptions(): void {
    this.platformService
      .query()
      .pipe(map((res: HttpResponse<IPlatform[]>) => res.body ?? []))
      .pipe(
        map((platforms: IPlatform[]) =>
          this.platformService.addPlatformToCollectionIfMissing<IPlatform>(platforms, ...(this.gameDetails?.platforms ?? []))
        )
      )
      .subscribe((platforms: IPlatform[]) => (this.platformsSharedCollection = platforms));

    this.developerService
      .query()
      .pipe(map((res: HttpResponse<IDeveloper[]>) => res.body ?? []))
      .pipe(
        map((developers: IDeveloper[]) =>
          this.developerService.addDeveloperToCollectionIfMissing<IDeveloper>(developers, ...(this.gameDetails?.developers ?? []))
        )
      )
      .subscribe((developers: IDeveloper[]) => (this.developersSharedCollection = developers));

    this.publisherService
      .query()
      .pipe(map((res: HttpResponse<IPublisher[]>) => res.body ?? []))
      .pipe(
        map((publishers: IPublisher[]) =>
          this.publisherService.addPublisherToCollectionIfMissing<IPublisher>(publishers, ...(this.gameDetails?.publishers ?? []))
        )
      )
      .subscribe((publishers: IPublisher[]) => (this.publishersSharedCollection = publishers));

    this.categoryService
      .query()
      .pipe(map((res: HttpResponse<ICategory[]>) => res.body ?? []))
      .pipe(
        map((categories: ICategory[]) =>
          this.categoryService.addCategoryToCollectionIfMissing<ICategory>(categories, ...(this.gameDetails?.categories ?? []))
        )
      )
      .subscribe((categories: ICategory[]) => (this.categoriesSharedCollection = categories));
  }
}
