import { IGameDetails } from 'app/entities/game-details/game-details.model';

export interface IGame {
  id: number;
  title?: string | null;
  gameDetails?: Pick<IGameDetails, 'id'> | null;
}

export type NewGame = Omit<IGame, 'id'> & { id: null };
