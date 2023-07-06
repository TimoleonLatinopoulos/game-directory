import { IDeveloper, NewDeveloper } from './developer.model';

export const sampleWithRequiredData: IDeveloper = {
  id: 68310,
};

export const sampleWithPartialData: IDeveloper = {
  id: 28552,
  description: 'Electronic mole generating',
};

export const sampleWithFullData: IDeveloper = {
  id: 20124,
  description: 'microchip',
};

export const sampleWithNewData: NewDeveloper = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
