import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDeveloper } from '../developer.model';
import { DeveloperService } from '../service/developer.service';

export const developerResolve = (route: ActivatedRouteSnapshot): Observable<null | IDeveloper> => {
  const id = route.params['id'];
  if (id) {
    return inject(DeveloperService)
      .find(id)
      .pipe(
        mergeMap((developer: HttpResponse<IDeveloper>) => {
          if (developer.body) {
            return of(developer.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default developerResolve;
