import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IUserGame } from '../user-game.model';
import { UserGameService } from '../service/user-game.service';

export const userGameResolve = (route: ActivatedRouteSnapshot): Observable<null | IUserGame> => {
  const id = route.params['id'];
  if (id) {
    return inject(UserGameService)
      .find(id)
      .pipe(
        mergeMap((userGame: HttpResponse<IUserGame>) => {
          if (userGame.body) {
            return of(userGame.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default userGameResolve;
