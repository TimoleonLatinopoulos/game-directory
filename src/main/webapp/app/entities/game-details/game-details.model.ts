import dayjs from 'dayjs/esm';
import { IPlatform } from 'app/entities/platform/platform.model';
import { IDeveloper } from 'app/entities/developer/developer.model';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { ICategory } from 'app/entities/category/category.model';

export interface IGameDetails {
  id: number;
  releaseDate?: dayjs.Dayjs | null;
  pegiRating?: string | null;
  metacriticScore?: number | null;
  imageUrl?: string | null;
  thumbnailUrl?: string | null;
  price?: number | null;
  description?: string | null;
  snippet?: string | null;
  notes?: string | null;
  steamAppid?: number | null;
  steamUrl?: number | null;
  platforms?: IPlatform[] | null;
  developers?: IDeveloper[] | null;
  publishers?: IPublisher[] | null;
  categories?: ICategory[] | null;
}

export type NewGameDetails = Omit<IGameDetails, 'id'> & { id: null };

export const pegiRatingList: string[] = ['Zero', 'Three', 'Seven', 'Twelve', 'Sixteen', 'Eighteen'];
export const pegiRatingNumbers: number[] = [0, 3, 7, 12, 16, 18];

export const getPegiRating = (requiredAge: any): string => {
  const age = Number(requiredAge);
  switch (age) {
    case 0:
      return pegiRatingList[0];
    case 3:
      return pegiRatingList[1];
    case 7:
      return pegiRatingList[2];
    case 12:
      return pegiRatingList[3];
    case 16:
      return pegiRatingList[4];
    case 18:
      return pegiRatingList[5];
    default:
      return pegiRatingList[0];
  }
};

export const getPegiNumber = (pegiRatingString: string): number => {
  switch (pegiRatingString) {
    case pegiRatingList[0]:
      return pegiRatingNumbers[0];
    case pegiRatingList[1]:
      return pegiRatingNumbers[1];
    case pegiRatingList[2]:
      return pegiRatingNumbers[2];
    case pegiRatingList[3]:
      return pegiRatingNumbers[3];
    case pegiRatingList[4]:
      return pegiRatingNumbers[4];
    case pegiRatingList[5]:
      return pegiRatingNumbers[5];
    default:
      return pegiRatingNumbers[0];
  }
};
