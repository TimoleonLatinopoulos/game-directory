import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPublisher } from '../publisher.model';
import { ICategory } from 'app/entities/category/category.model';

export type PartialUpdatePublisher = Partial<IPublisher> & Pick<IPublisher, 'id'>;

export type EntityResponseType = HttpResponse<IPublisher>;
export type EntityArrayResponseType = HttpResponse<ICategory[]>;

@Injectable({ providedIn: 'root' })
export class PublisherService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/publishers');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPublisher>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAll(): Observable<EntityArrayResponseType> {
    return this.http.get<ICategory[]>(this.resourceUrl, { observe: 'response' });
  }

  getResults(input: string): Observable<EntityArrayResponseType> {
    return this.http.get<ICategory[]>(`${this.resourceUrl}/search?input=${input}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPublisher[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  getPublisherIdentifier(publisher: Pick<IPublisher, 'id'>): number {
    return publisher.id;
  }

  comparePublisher(o1: Pick<IPublisher, 'id'> | null, o2: Pick<IPublisher, 'id'> | null): boolean {
    return o1 && o2 ? this.getPublisherIdentifier(o1) === this.getPublisherIdentifier(o2) : o1 === o2;
  }

  addPublisherToCollectionIfMissing<Type extends Pick<IPublisher, 'id'>>(
    publisherCollection: Type[],
    ...publishersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const publishers: Type[] = publishersToCheck.filter(isPresent);
    if (publishers.length > 0) {
      const publisherCollectionIdentifiers = publisherCollection.map(publisherItem => this.getPublisherIdentifier(publisherItem)!);
      const publishersToAdd = publishers.filter(publisherItem => {
        const publisherIdentifier = this.getPublisherIdentifier(publisherItem);
        if (publisherCollectionIdentifiers.includes(publisherIdentifier)) {
          return false;
        }
        publisherCollectionIdentifiers.push(publisherIdentifier);
        return true;
      });
      return [...publishersToAdd, ...publisherCollection];
    }
    return publisherCollection;
  }
}
