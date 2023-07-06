import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGameDetails, NewGameDetails } from '../game-details.model';

export type PartialUpdateGameDetails = Partial<IGameDetails> & Pick<IGameDetails, 'id'>;

type RestOf<T extends IGameDetails | NewGameDetails> = Omit<T, 'releaseDate'> & {
  releaseDate?: string | null;
};

export type RestGameDetails = RestOf<IGameDetails>;

export type NewRestGameDetails = RestOf<NewGameDetails>;

export type PartialUpdateRestGameDetails = RestOf<PartialUpdateGameDetails>;

export type EntityResponseType = HttpResponse<IGameDetails>;
export type EntityArrayResponseType = HttpResponse<IGameDetails[]>;

@Injectable({ providedIn: 'root' })
export class GameDetailsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/game-details');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(gameDetails: NewGameDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameDetails);
    return this.http
      .post<RestGameDetails>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(gameDetails: IGameDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameDetails);
    return this.http
      .put<RestGameDetails>(`${this.resourceUrl}/${this.getGameDetailsIdentifier(gameDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(gameDetails: PartialUpdateGameDetails): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(gameDetails);
    return this.http
      .patch<RestGameDetails>(`${this.resourceUrl}/${this.getGameDetailsIdentifier(gameDetails)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestGameDetails>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestGameDetails[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGameDetailsIdentifier(gameDetails: Pick<IGameDetails, 'id'>): number {
    return gameDetails.id;
  }

  compareGameDetails(o1: Pick<IGameDetails, 'id'> | null, o2: Pick<IGameDetails, 'id'> | null): boolean {
    return o1 && o2 ? this.getGameDetailsIdentifier(o1) === this.getGameDetailsIdentifier(o2) : o1 === o2;
  }

  addGameDetailsToCollectionIfMissing<Type extends Pick<IGameDetails, 'id'>>(
    gameDetailsCollection: Type[],
    ...gameDetailsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const gameDetails: Type[] = gameDetailsToCheck.filter(isPresent);
    if (gameDetails.length > 0) {
      const gameDetailsCollectionIdentifiers = gameDetailsCollection.map(
        gameDetailsItem => this.getGameDetailsIdentifier(gameDetailsItem)!
      );
      const gameDetailsToAdd = gameDetails.filter(gameDetailsItem => {
        const gameDetailsIdentifier = this.getGameDetailsIdentifier(gameDetailsItem);
        if (gameDetailsCollectionIdentifiers.includes(gameDetailsIdentifier)) {
          return false;
        }
        gameDetailsCollectionIdentifiers.push(gameDetailsIdentifier);
        return true;
      });
      return [...gameDetailsToAdd, ...gameDetailsCollection];
    }
    return gameDetailsCollection;
  }

  protected convertDateFromClient<T extends IGameDetails | NewGameDetails | PartialUpdateGameDetails>(gameDetails: T): RestOf<T> {
    return {
      ...gameDetails,
      releaseDate: gameDetails.releaseDate?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restGameDetails: RestGameDetails): IGameDetails {
    return {
      ...restGameDetails,
      releaseDate: restGameDetails.releaseDate ? dayjs(restGameDetails.releaseDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestGameDetails>): HttpResponse<IGameDetails> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestGameDetails[]>): HttpResponse<IGameDetails[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
