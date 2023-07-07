import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { GameFormService, GameFormGroup } from './game-form.service';
import { IGame } from '../game.model';
import { GameService } from '../service/game.service';
import { IGameDetails } from 'app/entities/game-details/game-details.model';
import { GameDetailsService } from 'app/entities/game-details/service/game-details.service';

@Component({
  standalone: true,
  selector: 'jhi-game-update',
  templateUrl: './game-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GameUpdateComponent implements OnInit {
  isSaving = false;
  game: IGame | null = null;

  gameDetailsCollection: IGameDetails[] = [];

  editForm: GameFormGroup = this.gameFormService.createGameFormGroup();

  constructor(
    protected gameService: GameService,
    protected gameFormService: GameFormService,
    protected gameDetailsService: GameDetailsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareGameDetails = (o1: IGameDetails | null, o2: IGameDetails | null): boolean => this.gameDetailsService.compareGameDetails(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ game }) => {
      this.game = game;
      if (game) {
        this.updateForm(game);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const game = this.gameFormService.getGame(this.editForm);
    if (game.id !== null) {
      this.subscribeToSaveResponse(this.gameService.update(game));
    } else {
      this.subscribeToSaveResponse(this.gameService.create(game));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGame>>): void {
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

  protected updateForm(game: IGame): void {
    this.game = game;
    this.gameFormService.resetForm(this.editForm, game);

    this.gameDetailsCollection = this.gameDetailsService.addGameDetailsToCollectionIfMissing<IGameDetails>(
      this.gameDetailsCollection,
      game.gameDetails
    );
  }

  protected loadRelationshipsOptions(): void {
    this.gameDetailsService
      .query({ filter: 'game-is-null' })
      .pipe(map((res: HttpResponse<IGameDetails[]>) => res.body ?? []))
      .pipe(
        map((gameDetails: IGameDetails[]) =>
          this.gameDetailsService.addGameDetailsToCollectionIfMissing<IGameDetails>(gameDetails, this.game?.gameDetails)
        )
      )
      .subscribe((gameDetails: IGameDetails[]) => (this.gameDetailsCollection = gameDetails));
  }
}
