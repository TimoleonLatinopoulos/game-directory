import dayjs from 'dayjs/esm';
import { IUser } from 'app/entities/user/user.model';
import { IGame } from 'app/entities/game/game.model';

export interface IUserGame {
  id: number;
  favourite?: boolean | null;
  dateAdded?: dayjs.Dayjs | null;
  title?: string | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
  game?: Pick<IGame, 'id'> | null;
}

export type NewUserGame = Omit<IUserGame, 'id'> & { id: null };
