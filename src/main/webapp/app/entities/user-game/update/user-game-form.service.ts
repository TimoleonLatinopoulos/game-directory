import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IUserGame, NewUserGame } from '../user-game.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUserGame for edit and NewUserGameFormGroupInput for create.
 */
type UserGameFormGroupInput = IUserGame | PartialWithRequiredKeyOf<NewUserGame>;

type UserGameFormDefaults = Pick<NewUserGame, 'id' | 'favourite'>;

type UserGameFormGroupContent = {
  id: FormControl<IUserGame['id'] | NewUserGame['id']>;
  favourite: FormControl<IUserGame['favourite']>;
  dateAdded: FormControl<IUserGame['dateAdded']>;
  title: FormControl<IUserGame['title']>;
  user: FormControl<IUserGame['user']>;
  game: FormControl<IUserGame['game']>;
};

export type UserGameFormGroup = FormGroup<UserGameFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UserGameFormService {
  createUserGameFormGroup(userGame: UserGameFormGroupInput = { id: null }): UserGameFormGroup {
    const userGameRawValue = {
      ...this.getFormDefaults(),
      ...userGame,
    };
    return new FormGroup<UserGameFormGroupContent>({
      id: new FormControl(
        { value: userGameRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      favourite: new FormControl(userGameRawValue.favourite),
      dateAdded: new FormControl(userGameRawValue.dateAdded),
      title: new FormControl(userGameRawValue.title),
      user: new FormControl(userGameRawValue.user),
      game: new FormControl(userGameRawValue.game),
    });
  }

  getUserGame(form: UserGameFormGroup): IUserGame | NewUserGame {
    return form.getRawValue() as IUserGame | NewUserGame;
  }

  resetForm(form: UserGameFormGroup, userGame: UserGameFormGroupInput): void {
    const userGameRawValue = { ...this.getFormDefaults(), ...userGame };
    form.reset(
      {
        ...userGameRawValue,
        id: { value: userGameRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UserGameFormDefaults {
    return {
      id: null,
      favourite: false,
    };
  }
}
