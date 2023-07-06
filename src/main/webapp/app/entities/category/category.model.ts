import { IGameDetails } from 'app/entities/game-details/game-details.model';

export interface ICategory {
  id: number;
  description?: string | null;
  gameDetails?: Pick<IGameDetails, 'id'>[] | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };
