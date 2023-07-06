import { IPlatform, NewPlatform } from './platform.model';

export const sampleWithRequiredData: IPlatform = {
  id: 19905,
};

export const sampleWithPartialData: IPlatform = {
  id: 97598,
  description: 'Technician Botswana',
};

export const sampleWithFullData: IPlatform = {
  id: 74629,
  description: 'Soul Cambridgeshire',
};

export const sampleWithNewData: NewPlatform = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
