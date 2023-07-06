import { IGameDetails } from 'app/entities/game-details/game-details.model';

export interface IDeveloper {
  id: number;
  description?: string | null;
  gameDetails?: Pick<IGameDetails, 'id'>[] | null;
}

export type NewDeveloper = Omit<IDeveloper, 'id'> & { id: null };
