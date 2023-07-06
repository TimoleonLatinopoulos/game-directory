import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PlatformComponent } from './list/platform.component';
import { PlatformDetailComponent } from './detail/platform-detail.component';
import PlatformResolve from './route/platform-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const platformRoute: Routes = [
  {
    path: '',
    component: PlatformComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PlatformDetailComponent,
    resolve: {
      platform: PlatformResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default platformRoute;
