import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPlatform } from '../platform.model';
import { PlatformService } from '../service/platform.service';

export const platformResolve = (route: ActivatedRouteSnapshot): Observable<null | IPlatform> => {
  const id = route.params['id'];
  if (id) {
    return inject(PlatformService)
      .find(id)
      .pipe(
        mergeMap((platform: HttpResponse<IPlatform>) => {
          if (platform.body) {
            return of(platform.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default platformResolve;
