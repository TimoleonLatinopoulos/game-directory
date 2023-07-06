import dayjs from 'dayjs/esm';
import { IPlatform } from 'app/entities/platform/platform.model';
import { IDeveloper } from 'app/entities/developer/developer.model';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { ICategory } from 'app/entities/category/category.model';

export interface IGameDetails {
  id: number;
  requiredAge?: number | null;
  releaseDate?: dayjs.Dayjs | null;
  pegiRating?: string | null;
  metacriticScore?: number | null;
  imageUrl?: string | null;
  thumbnailUrl?: string | null;
  price?: number | null;
  description?: string | null;
  notes?: string | null;
  steamAppid?: number | null;
  platforms?: Pick<IPlatform, 'id'>[] | null;
  developers?: Pick<IDeveloper, 'id'>[] | null;
  publishers?: Pick<IPublisher, 'id'>[] | null;
  categories?: Pick<ICategory, 'id'>[] | null;
}

export type NewGameDetails = Omit<IGameDetails, 'id'> & { id: null };
