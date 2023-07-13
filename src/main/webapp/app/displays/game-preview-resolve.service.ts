import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { IGame } from 'app/entities/game/game.model';
import { GameService } from 'app/entities/game/service/game.service';

export const gamePreviewResolve = (route: ActivatedRouteSnapshot): Observable<null | IGame> => {
  const id = route.params['id'];
  if (id) {
    return inject(GameService)
      .find(id)
      .pipe(
        mergeMap((game: HttpResponse<IGame>) => {
          if (game.body) {
            return of(game.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default gamePreviewResolve;
