import dayjs from 'dayjs/esm';

import { IGameDetails, NewGameDetails } from './game-details.model';

export const sampleWithRequiredData: IGameDetails = {
  id: 22574,
};

export const sampleWithPartialData: IGameDetails = {
  id: 91234,
  metacriticScore: 90983,
  imageUrl: 'Credit orchestrate',
  thumbnailUrl: 'Books Movies',
  price: 43967,
};

export const sampleWithFullData: IGameDetails = {
  id: 99663,
  releaseDate: dayjs('2023-07-05'),
  pegiRating: 'Dong parsing',
  metacriticScore: 13034,
  imageUrl: 'caption',
  thumbnailUrl: 'Tasty Benz',
  price: 80676,
  description: 'Gasoline Rustic',
  notes: 'Soft Distributed',
  steamAppid: 66920,
};

export const sampleWithNewData: NewGameDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
