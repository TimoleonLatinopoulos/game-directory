import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPlatform } from '../platform.model';
import { ICategory } from 'app/entities/category/category.model';

export type PartialUpdatePlatform = Partial<ICategory> & Pick<ICategory, 'id'>;

export type EntityResponseType = HttpResponse<IPlatform>;
export type EntityArrayResponseType = HttpResponse<ICategory[]>;

@Injectable({ providedIn: 'root' })
export class PlatformService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/platforms');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPlatform>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAll(): Observable<EntityArrayResponseType> {
    return this.http.get<ICategory[]>(this.resourceUrl, { observe: 'response' });
  }

  getResults(input: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICategory[]>(`${this.resourceUrl}/search?input=${input}`, { observe: 'response' });
  }

  getUsedResults(input: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICategory[]>(`${this.resourceUrl}/search_used?input=${input}`, { observe: 'response' });
  }

  getAllUsed(): Observable<EntityArrayResponseType> {
    return this.http.get<ICategory[]>(this.resourceUrl + '/used', { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPlatform[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getPlatformIdentifier(platform: Pick<IPlatform, 'id'>): number {
    return platform.id;
  }

  comparePlatform(o1: Pick<IPlatform, 'id'> | null, o2: Pick<IPlatform, 'id'> | null): boolean {
    return o1 && o2 ? this.getPlatformIdentifier(o1) === this.getPlatformIdentifier(o2) : o1 === o2;
  }

  addPlatformToCollectionIfMissing<Type extends Pick<IPlatform, 'id'>>(
    platformCollection: Type[],
    ...platformsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const platforms: Type[] = platformsToCheck.filter(isPresent);
    if (platforms.length > 0) {
      const platformCollectionIdentifiers = platformCollection.map(platformItem => this.getPlatformIdentifier(platformItem)!);
      const platformsToAdd = platforms.filter(platformItem => {
        const platformIdentifier = this.getPlatformIdentifier(platformItem);
        if (platformCollectionIdentifiers.includes(platformIdentifier)) {
          return false;
        }
        platformCollectionIdentifiers.push(platformIdentifier);
        return true;
      });
      return [...platformsToAdd, ...platformCollection];
    }
    return platformCollection;
  }
}
