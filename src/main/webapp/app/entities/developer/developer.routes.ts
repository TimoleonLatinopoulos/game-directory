import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DeveloperComponent } from './list/developer.component';
import { DeveloperDetailComponent } from './detail/developer-detail.component';
import DeveloperResolve from './route/developer-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const developerRoute: Routes = [
  {
    path: '',
    component: DeveloperComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DeveloperDetailComponent,
    resolve: {
      developer: DeveloperResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default developerRoute;
