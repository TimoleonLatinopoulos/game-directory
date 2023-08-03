import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { UserGameComponent } from './list/user-game.component';
import { UserGameDetailComponent } from './detail/user-game-detail.component';
import { UserGameUpdateComponent } from './update/user-game-update.component';
import UserGameResolve from './route/user-game-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const userGameRoute: Routes = [
  {
    path: '',
    component: UserGameComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: UserGameDetailComponent,
    resolve: {
      userGame: UserGameResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: UserGameUpdateComponent,
    resolve: {
      userGame: UserGameResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: UserGameUpdateComponent,
    resolve: {
      userGame: UserGameResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default userGameRoute;
