import dayjs from 'dayjs/esm';

import { IGameDetails, NewGameDetails } from './game-details.model';

export const sampleWithRequiredData: IGameDetails = {
  id: 29431,
};

export const sampleWithPartialData: IGameDetails = {
  id: 39238,
  metacriticScore: 12338,
  price: 18487,
  description: 'programming Genderflux Solutions',
  notes: 'Clinton',
  steamAppid: 5535,
};

export const sampleWithFullData: IGameDetails = {
  id: 43967,
  requiredAge: 99663,
  releaseDate: dayjs('2023-07-05'),
  pegiRating: 'District luxurious',
  metacriticScore: 96438,
  imageUrl: 'rich',
  thumbnailUrl: 'till',
  price: 66483,
  description: 'Gasoline Gasoline',
  notes: 'maroon',
  steamAppid: 25827,
};

export const sampleWithNewData: NewGameDetails = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
