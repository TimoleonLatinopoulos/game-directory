import { IGame, NewGame } from './game.model';

export const sampleWithRequiredData: IGame = {
  id: 60663,
};

export const sampleWithPartialData: IGame = {
  id: 9502,
  title: 'Program Producer',
};

export const sampleWithFullData: IGame = {
  id: 58463,
  title: 'Gorgeous violet',
};

export const sampleWithNewData: NewGame = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
