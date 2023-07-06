import { ICategory, NewCategory } from './category.model';

export const sampleWithRequiredData: ICategory = {
  id: 78420,
};

export const sampleWithPartialData: ICategory = {
  id: 27876,
};

export const sampleWithFullData: ICategory = {
  id: 99716,
  description: 'Pennsylvania Iridium beetle',
};

export const sampleWithNewData: NewCategory = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
