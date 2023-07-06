import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'category',
        data: { pageTitle: 'Categories' },
        loadChildren: () => import('./category/category.routes'),
      },
      {
        path: 'platform',
        data: { pageTitle: 'Platforms' },
        loadChildren: () => import('./platform/platform.routes'),
      },
      {
        path: 'game',
        data: { pageTitle: 'Games' },
        loadChildren: () => import('./game/game.routes'),
      },
      {
        path: 'developer',
        data: { pageTitle: 'Developers' },
        loadChildren: () => import('./developer/developer.routes'),
      },
      {
        path: 'publisher',
        data: { pageTitle: 'Publishers' },
        loadChildren: () => import('./publisher/publisher.routes'),
      },
      {
        path: 'game-details',
        data: { pageTitle: 'GameDetails' },
        loadChildren: () => import('./game-details/game-details.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
