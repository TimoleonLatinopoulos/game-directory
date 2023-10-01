import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IGameDetails } from '../game-details.model';
import { ISteamGame, NewSteamGameDetails } from '../steam-game-details.model';
import { RestGameDetails } from './game-details.service';

export type PartialUpdateGameDetails = Partial<IGameDetails> & Pick<IGameDetails, 'id'>;

type RestOf<T extends ISteamGame | NewSteamGameDetails> = Omit<T, 'releaseDate'> & {
  releaseDate?: string | null;
};

export type RestSteamGame = RestOf<ISteamGame>;

export type EntityResponseType = HttpResponse<ISteamGame>;
export type EntityArrayResponseType = HttpResponse<ISteamGame[]>;

@Injectable({ providedIn: 'root' })
export class SteamApiService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/steam-games');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getFromExternalApi(steam_appid: number): Observable<HttpResponse<RestGameDetails>> {
    return this.http.get<RestGameDetails>(`${this.resourceUrl}/${steam_appid}`, { observe: 'response' });
  }
}
