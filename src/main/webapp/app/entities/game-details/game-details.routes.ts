import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GameDetailsComponent } from './list/game-details.component';
import { GameDetailsDetailComponent } from './detail/game-details-detail.component';
import { GameDetailsUpdateComponent } from './update/game-details-update.component';
import GameDetailsResolve from './route/game-details-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const gameDetailsRoute: Routes = [
  {
    path: '',
    component: GameDetailsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GameDetailsDetailComponent,
    resolve: {
      gameDetails: GameDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GameDetailsUpdateComponent,
    resolve: {
      gameDetails: GameDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GameDetailsUpdateComponent,
    resolve: {
      gameDetails: GameDetailsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default gameDetailsRoute;
