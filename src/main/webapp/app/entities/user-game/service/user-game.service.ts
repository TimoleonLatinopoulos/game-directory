import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption, createSearchRequestOption } from 'app/core/request/request-util';
import { IUserGame, NewUserGame } from '../user-game.model';

export type PartialUpdateUserGame = Partial<IUserGame> & Pick<IUserGame, 'id'>;

type RestOf<T extends IUserGame | NewUserGame> = Omit<T, 'dateAdded'> & {
  dateAdded?: string | null;
};

export type PartialUpdateRestUserGame = RestOf<PartialUpdateUserGame>;

export type EntityResponseType = HttpResponse<IUserGame>;
export type EntityArrayResponseType = HttpResponse<IUserGame[]>;

@Injectable({ providedIn: 'root' })
export class UserGameService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/user-games');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(id: number): Observable<EntityResponseType> {
    return this.http.post<IUserGame>(this.resourceUrl, id, { observe: 'response' });
  }

  update(userGame: IUserGame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userGame);
    return this.http
      .put<IUserGame>(`${this.resourceUrl}/${this.getUserGameIdentifier(userGame)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(userGame: PartialUpdateUserGame): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(userGame);
    return this.http
      .patch<IUserGame>(`${this.resourceUrl}/${this.getUserGameIdentifier(userGame)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserGame>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  findByGameId(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUserGame>(`${this.resourceUrl}/game/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  search(state: any): Observable<any> {
    const options = createSearchRequestOption(state);
    return this.http.get(this.resourceUrl + '/search', { params: options }).pipe(map((data: any) => ({ data, total: data.totalEntries })));
  }

  setFavourite(id: number): Observable<EntityResponseType> {
    return this.http.get<IUserGame>(`${this.resourceUrl}/set-favourite/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUserGame[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getUserGameIdentifier(userGame: Pick<IUserGame, 'id'>): number {
    return userGame.id;
  }

  compareUserGame(o1: Pick<IUserGame, 'id'> | null, o2: Pick<IUserGame, 'id'> | null): boolean {
    return o1 && o2 ? this.getUserGameIdentifier(o1) === this.getUserGameIdentifier(o2) : o1 === o2;
  }

  addUserGameToCollectionIfMissing<Type extends Pick<IUserGame, 'id'>>(
    userGameCollection: Type[],
    ...userGamesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const userGames: Type[] = userGamesToCheck.filter(isPresent);
    if (userGames.length > 0) {
      const userGameCollectionIdentifiers = userGameCollection.map(userGameItem => this.getUserGameIdentifier(userGameItem)!);
      const userGamesToAdd = userGames.filter(userGameItem => {
        const userGameIdentifier = this.getUserGameIdentifier(userGameItem);
        if (userGameCollectionIdentifiers.includes(userGameIdentifier)) {
          return false;
        }
        userGameCollectionIdentifiers.push(userGameIdentifier);
        return true;
      });
      return [...userGamesToAdd, ...userGameCollection];
    }
    return userGameCollection;
  }

  protected convertDateFromClient<T extends IUserGame | NewUserGame | PartialUpdateUserGame>(userGame: T): RestOf<T> {
    return {
      ...userGame,
      dateAdded: userGame.dateAdded?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(userGame: IUserGame): IUserGame {
    return {
      ...userGame,
      dateAdded: userGame.dateAdded ? dayjs(userGame.dateAdded) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<IUserGame>): HttpResponse<IUserGame> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<IUserGame[]>): HttpResponse<IUserGame[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
