import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGameDetails, NewGameDetails } from '../game-details.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGameDetails for edit and NewGameDetailsFormGroupInput for create.
 */
type GameDetailsFormGroupInput = IGameDetails | PartialWithRequiredKeyOf<NewGameDetails>;

type GameDetailsFormDefaults = Pick<NewGameDetails, 'id' | 'platforms' | 'developers' | 'publishers' | 'categories'>;

type GameDetailsFormGroupContent = {
  id: FormControl<IGameDetails['id'] | NewGameDetails['id']>;
  requiredAge: FormControl<IGameDetails['requiredAge']>;
  releaseDate: FormControl<IGameDetails['releaseDate']>;
  pegiRating: FormControl<IGameDetails['pegiRating']>;
  metacriticScore: FormControl<IGameDetails['metacriticScore']>;
  imageUrl: FormControl<IGameDetails['imageUrl']>;
  thumbnailUrl: FormControl<IGameDetails['thumbnailUrl']>;
  price: FormControl<IGameDetails['price']>;
  description: FormControl<IGameDetails['description']>;
  notes: FormControl<IGameDetails['notes']>;
  steamAppid: FormControl<IGameDetails['steamAppid']>;
  platforms: FormControl<IGameDetails['platforms']>;
  developers: FormControl<IGameDetails['developers']>;
  publishers: FormControl<IGameDetails['publishers']>;
  categories: FormControl<IGameDetails['categories']>;
};

export type GameDetailsFormGroup = FormGroup<GameDetailsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameDetailsFormService {
  createGameDetailsFormGroup(gameDetails: GameDetailsFormGroupInput = { id: null }): GameDetailsFormGroup {
    const gameDetailsRawValue = {
      ...this.getFormDefaults(),
      ...gameDetails,
    };
    return new FormGroup<GameDetailsFormGroupContent>({
      id: new FormControl(
        { value: gameDetailsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      requiredAge: new FormControl(gameDetailsRawValue.requiredAge),
      releaseDate: new FormControl(gameDetailsRawValue.releaseDate),
      pegiRating: new FormControl(gameDetailsRawValue.pegiRating),
      metacriticScore: new FormControl(gameDetailsRawValue.metacriticScore),
      imageUrl: new FormControl(gameDetailsRawValue.imageUrl),
      thumbnailUrl: new FormControl(gameDetailsRawValue.thumbnailUrl),
      price: new FormControl(gameDetailsRawValue.price),
      description: new FormControl(gameDetailsRawValue.description),
      notes: new FormControl(gameDetailsRawValue.notes),
      steamAppid: new FormControl(gameDetailsRawValue.steamAppid),
      platforms: new FormControl(gameDetailsRawValue.platforms ?? []),
      developers: new FormControl(gameDetailsRawValue.developers ?? []),
      publishers: new FormControl(gameDetailsRawValue.publishers ?? []),
      categories: new FormControl(gameDetailsRawValue.categories ?? []),
    });
  }

  getGameDetails(form: GameDetailsFormGroup): IGameDetails | NewGameDetails {
    return form.getRawValue() as IGameDetails | NewGameDetails;
  }

  resetForm(form: GameDetailsFormGroup, gameDetails: GameDetailsFormGroupInput): void {
    const gameDetailsRawValue = { ...this.getFormDefaults(), ...gameDetails };
    form.reset(
      {
        ...gameDetailsRawValue,
        id: { value: gameDetailsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GameDetailsFormDefaults {
    return {
      id: null,
      platforms: [],
      developers: [],
      publishers: [],
      categories: [],
    };
  }
}
