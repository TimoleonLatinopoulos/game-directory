import dayjs from 'dayjs/esm';

import { IComment, NewComment } from './comment.model';

export const sampleWithRequiredData: IComment = {
  id: 68208,
};

export const sampleWithPartialData: IComment = {
  id: 85696,
  description: 'custom South',
  datePosted: dayjs('2023-08-27'),
};

export const sampleWithFullData: IComment = {
  id: 69213,
  description: 'Northwest',
  recommended: true,
  datePosted: dayjs('2023-08-28'),
};

export const sampleWithNewData: NewComment = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
