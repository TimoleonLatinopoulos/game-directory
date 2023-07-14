import { GameListComponent } from './game-list/game-list.component';
import { Routes } from '@angular/router';

import { CreateGameEntryComponent } from './create-game-entry/create-game-entry.component';
import { GamePreviewComponent } from './game-preview/game-preview.component';
import GamePreviewResolve from './game-preview-resolve.service';

export const displaysRoute: Routes = [
  {
    path: '',
    component: GameListComponent,
    title: 'Game List',
  },
  {
    path: 'new-entry',
    component: CreateGameEntryComponent,
    title: 'New Game Entry',
  },
  {
    path: 'game-preview/:id',
    component: GamePreviewComponent,
    resolve: {
      game: GamePreviewResolve,
    },
    title: 'Game',
  },
];
