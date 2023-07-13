import { IGameDetails } from 'app/entities/game-details/game-details.model';

export interface IGame {
  id: number;
  title?: string | null;
  gameDetails?: IGameDetails | null;
}

export type NewGame = Omit<IGame, 'id'> & { id: null };

export class Game implements IGame {
  constructor(public id: number, public title?: string | null, public gameDetails?: IGameDetails | null) {}
}
