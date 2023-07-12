import dayjs from 'dayjs/esm';
import { IPlatform } from 'app/entities/platform/platform.model';
import { IDeveloper } from 'app/entities/developer/developer.model';
import { IPublisher } from 'app/entities/publisher/publisher.model';
import { ICategory } from 'app/entities/category/category.model';

export interface ISteamGame {
  success: boolean;
  data: ISteamGameDetails;
}

export interface ISteamGameDetails {
  type: string;
  name: string;
  steam_appid: number;
  required_age: 18;
  is_free: boolean;
  controller_support: string;
  detailed_description: string;
  about_the_game: string;
  short_description: string;
  supported_languages: string;
  reviews: string;
  header_image: string;
  capsule_image: string;
  capsule_imagev5: string;
  website: string;
  developers: string[];
  publishers: string[];
  categories: ICategory[];
  genres: ICategory[];
  metacritic: IMetacritic;
  release_date: IReleaseDate;
  price_overview: IPrice;
  content_descriptors: IContentDescriptors;
}

export interface IMetacritic {
  score: number;
  url: string;
}

export interface IReleaseDate {
  coming_soon: boolean;
  date: string;
}

export interface IPrice {
  currency: string;
  initial: number;
  final: number;
  discount_percent: number;
  initial_formatted: string;
  final_formatted: string;
}

export interface IContentDescriptors {
  notes: string;
}

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
  platforms?: Pick<IPlatform, 'id'>[] | null;
  developers?: Pick<IDeveloper, 'id'>[] | null;
  publishers?: Pick<IPublisher, 'id'>[] | null;
  categories?: Pick<ICategory, 'id'>[] | null;
}

export type NewGameDetails = Omit<IGameDetails, 'id'> & { id: null };

export type NewSteamGameDetails = Omit<ISteamGame, 'id'> & { id: null };
