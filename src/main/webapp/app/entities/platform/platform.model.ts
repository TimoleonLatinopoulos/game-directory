import { IGameDetails } from 'app/entities/game-details/game-details.model';

export interface IPlatform {
  id: number;
  description?: string | null;
  gameDetails?: Pick<IGameDetails, 'id'>[] | null;
}

export type NewPlatform = Omit<IPlatform, 'id'> & { id: null };
