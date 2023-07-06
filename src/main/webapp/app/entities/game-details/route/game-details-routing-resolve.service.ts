import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGameDetails } from '../game-details.model';
import { GameDetailsService } from '../service/game-details.service';

export const gameDetailsResolve = (route: ActivatedRouteSnapshot): Observable<null | IGameDetails> => {
  const id = route.params['id'];
  if (id) {
    return inject(GameDetailsService)
      .find(id)
      .pipe(
        mergeMap((gameDetails: HttpResponse<IGameDetails>) => {
          if (gameDetails.body) {
            return of(gameDetails.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default gameDetailsResolve;
