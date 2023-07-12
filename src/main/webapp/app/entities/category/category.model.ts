import { IGameDetails } from 'app/entities/game-details/game-details.model';

export interface ICategory {
  id: number;
  description?: string | null;
  gameDetails?: Pick<IGameDetails, 'id'>[] | null;
}

export type NewCategory = Omit<ICategory, 'id'> & { id: null };

export class Category implements ICategory {
  constructor(public id: number, public description?: string | null, public gameDetails?: Pick<IGameDetails, 'id'>[] | null) {}
}
