import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IGame, NewGame } from '../game.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IGame for edit and NewGameFormGroupInput for create.
 */
type GameFormGroupInput = IGame | PartialWithRequiredKeyOf<NewGame>;

type GameFormDefaults = Pick<NewGame, 'id'>;

type GameFormGroupContent = {
  id: FormControl<IGame['id'] | NewGame['id']>;
  title: FormControl<IGame['title']>;
};

export type GameFormGroup = FormGroup<GameFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class GameFormService {
  createGameFormGroup(game: GameFormGroupInput = { id: null }): GameFormGroup {
    const gameRawValue = {
      ...this.getFormDefaults(),
      ...game,
    };
    return new FormGroup<GameFormGroupContent>({
      id: new FormControl(
        { value: gameRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(gameRawValue.title),
    });
  }

  getGame(form: GameFormGroup): IGame | NewGame {
    return form.getRawValue() as IGame | NewGame;
  }

  resetForm(form: GameFormGroup, game: GameFormGroupInput): void {
    const gameRawValue = { ...this.getFormDefaults(), ...game };
    form.reset(
      {
        ...gameRawValue,
        id: { value: gameRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): GameFormDefaults {
    return {
      id: null,
    };
  }
}
