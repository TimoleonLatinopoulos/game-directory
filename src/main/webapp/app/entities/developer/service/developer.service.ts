import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDeveloper, NewDeveloper } from '../developer.model';
import { ICategory } from 'app/entities/category/category.model';

export type PartialUpdateDeveloper = Partial<IDeveloper> & Pick<IDeveloper, 'id'>;

export type EntityResponseType = HttpResponse<IDeveloper>;
export type EntityArrayResponseType = HttpResponse<ICategory[]>;

@Injectable({ providedIn: 'root' })
export class DeveloperService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/developers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDeveloper>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAll(): Observable<EntityArrayResponseType> {
    return this.http.get<ICategory[]>(this.resourceUrl, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDeveloper[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getDeveloperIdentifier(developer: Pick<IDeveloper, 'id'>): number {
    return developer.id;
  }

  compareDeveloper(o1: Pick<IDeveloper, 'id'> | null, o2: Pick<IDeveloper, 'id'> | null): boolean {
    return o1 && o2 ? this.getDeveloperIdentifier(o1) === this.getDeveloperIdentifier(o2) : o1 === o2;
  }

  addDeveloperToCollectionIfMissing<Type extends Pick<IDeveloper, 'id'>>(
    developerCollection: Type[],
    ...developersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const developers: Type[] = developersToCheck.filter(isPresent);
    if (developers.length > 0) {
      const developerCollectionIdentifiers = developerCollection.map(developerItem => this.getDeveloperIdentifier(developerItem)!);
      const developersToAdd = developers.filter(developerItem => {
        const developerIdentifier = this.getDeveloperIdentifier(developerItem);
        if (developerCollectionIdentifiers.includes(developerIdentifier)) {
          return false;
        }
        developerCollectionIdentifiers.push(developerIdentifier);
        return true;
      });
      return [...developersToAdd, ...developerCollection];
    }
    return developerCollection;
  }
}
