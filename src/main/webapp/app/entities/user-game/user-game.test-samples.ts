import dayjs from 'dayjs/esm';

import { IUserGame, NewUserGame } from './user-game.model';

export const sampleWithRequiredData: IUserGame = {
  id: 76173,
};

export const sampleWithPartialData: IUserGame = {
  id: 71968,
  favourite: false,
  dateAdded: dayjs('2023-08-01'),
};

export const sampleWithFullData: IUserGame = {
  id: 78854,
  favourite: true,
  dateAdded: dayjs('2023-08-02'),
  title: 'Jewelery Granite partnerships',
};

export const sampleWithNewData: NewUserGame = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
