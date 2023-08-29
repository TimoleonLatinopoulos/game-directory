import dayjs from 'dayjs/esm';
import { IGame } from 'app/entities/game/game.model';
import { IUser } from 'app/entities/user/user.model';

export interface IComment {
  id: number;
  description?: string | null;
  recommended?: boolean | null;
  datePosted?: dayjs.Dayjs | null;
  game?: Pick<IGame, 'id'> | null;
  user?: IUser | null;
}
