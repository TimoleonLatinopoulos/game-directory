import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { UserGameFormService, UserGameFormGroup } from './user-game-form.service';
import { IUserGame } from '../user-game.model';
import { UserGameService } from '../service/user-game.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

@Component({
  standalone: true,
  selector: 'jhi-user-game-update',
  templateUrl: './user-game-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class UserGameUpdateComponent implements OnInit {
  isSaving = false;
  userGame: IUserGame | null = null;

  usersSharedCollection: IUser[] = [];
  gamesSharedCollection: IGame[] = [];

  editForm: UserGameFormGroup = this.userGameFormService.createUserGameFormGroup();

  constructor(
    protected userGameService: UserGameService,
    protected userGameFormService: UserGameFormService,
    protected userService: UserService,
    protected gameService: GameService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareGame = (o1: IGame | null, o2: IGame | null): boolean => this.gameService.compareGame(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ userGame }) => {
      this.userGame = userGame;
      if (userGame) {
        this.updateForm(userGame);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userGame = this.userGameFormService.getUserGame(this.editForm);
    if (userGame.id !== null) {
      this.subscribeToSaveResponse(this.userGameService.update(userGame));
    } else {
      this.subscribeToSaveResponse(this.userGameService.create(userGame));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserGame>>): void {
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

  protected updateForm(userGame: IUserGame): void {
    this.userGame = userGame;
    this.userGameFormService.resetForm(this.editForm, userGame);

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, userGame.user);
    this.gamesSharedCollection = this.gameService.addGameToCollectionIfMissing<IGame>(this.gamesSharedCollection, userGame.game);
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.userGame?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.gameService
      .query()
      .pipe(map((res: HttpResponse<IGame[]>) => res.body ?? []))
      .pipe(map((games: IGame[]) => this.gameService.addGameToCollectionIfMissing<IGame>(games, this.userGame?.game)))
      .subscribe((games: IGame[]) => (this.gamesSharedCollection = games));
  }
}
