import { GameListComponent } from './game-list/game-list.component';
import { Routes } from '@angular/router';

import { CreateGameEntryComponent } from './create-game-entry/create-game-entry.component';
import { GamePreviewComponent } from './game-preview/game-preview.component';
import GamePreviewResolve from './game-preview-resolve.service';
import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { Authority } from 'app/config/authority.constants';

export const displaysRoute: Routes = [
  {
    path: '',
    component: GameListComponent,
    canActivate: [UserRouteAccessService],
    data: {
      userGames: false,
    },
    title: 'Game List',
  },
  {
    path: 'my-games',
    component: GameListComponent,
    data: {
      authorities: [Authority.USER],
      userGames: true,
    },
    canActivate: [UserRouteAccessService],
    title: 'My Games',
  },
  {
    path: 'new-entry',
    component: CreateGameEntryComponent,
    data: {
      authorities: [Authority.ADMIN],
      update: false,
    },
    canActivate: [UserRouteAccessService],
    title: 'New Game Entry',
  },
  {
    path: 'edit-entry/:id',
    component: CreateGameEntryComponent,
    data: {
      authorities: [Authority.ADMIN],
      update: true,
    },
    canActivate: [UserRouteAccessService],
    title: 'Edit Game Entry',
  },
  {
    path: 'game-preview/:id',
    component: GamePreviewComponent,
    resolve: {
      game: GamePreviewResolve,
    },
    canActivate: [UserRouteAccessService],
    title: 'Game',
  },
  {
    path: 'game-preview/:id/:title',
    redirectTo: 'game-preview/:id',
    pathMatch: 'full',
    title: 'Game',
  },
];
