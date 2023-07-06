import { IGameDetails } from 'app/entities/game-details/game-details.model';

export interface IPublisher {
  id: number;
  description?: string | null;
  gameDetails?: Pick<IGameDetails, 'id'>[] | null;
}

export type NewPublisher = Omit<IPublisher, 'id'> & { id: null };
