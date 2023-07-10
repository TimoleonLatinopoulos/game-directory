import { Routes } from '@angular/router';

import { CreateGameEntryComponent } from './create-game-entry/create-game-entry.component';

export const displaysRoute: Routes = [
  {
    path: 'new-entry',
    component: CreateGameEntryComponent,
    title: 'New Game Entry',
  },
];
