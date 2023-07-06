import { IPublisher, NewPublisher } from './publisher.model';

export const sampleWithRequiredData: IPublisher = {
  id: 72235,
};

export const sampleWithPartialData: IPublisher = {
  id: 9768,
  description: 'nor',
};

export const sampleWithFullData: IPublisher = {
  id: 29535,
  description: '5th Bicycle',
};

export const sampleWithNewData: NewPublisher = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
