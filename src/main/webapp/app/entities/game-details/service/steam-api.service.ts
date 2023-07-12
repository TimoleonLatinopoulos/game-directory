import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IGameDetails } from '../game-details.model';
import { ISteamGame, NewSteamGameDetails } from '../steam-game-details.model';

export type PartialUpdateGameDetails = Partial<IGameDetails> & Pick<IGameDetails, 'id'>;

type RestOf<T extends ISteamGame | NewSteamGameDetails> = Omit<T, 'releaseDate'> & {
  releaseDate?: string | null;
};

// Run proxy for CORSS
// lcp --proxyUrl http://store.steampowered.com/api

export type RestSteamGame = RestOf<ISteamGame>;

export type EntityResponseType = HttpResponse<ISteamGame>;
export type EntityArrayResponseType = HttpResponse<ISteamGame[]>;

@Injectable({ providedIn: 'root' })
export class SteamApiService {
  protected proxy = 'http://localhost:8010/proxy';
  protected resourceUrl = this.proxy + 'http://store.steampowered.com/api/appdetails';
  protected proxyUrl = this.proxy + '/appdetails';

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(steam_appid: number): Observable<EntityResponseType> {
    const headers = new HttpHeaders()
      .set('Authorization', 'key 1618F94F3C32128DA7FFDC0662B8D95B')
      .set('Accept', 'application/json, text/plain, */*')
      .set('Accept-Language', 'el-GR,el;q=0.8,en-US;q=0.5,en;q=0.3');

    return this.http.get<RestSteamGame>(`${this.proxyUrl}?appids=${steam_appid}`, {
      headers: headers,
      observe: 'response',
    });
  }
}
