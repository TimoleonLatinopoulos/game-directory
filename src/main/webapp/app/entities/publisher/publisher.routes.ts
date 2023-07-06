import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PublisherComponent } from './list/publisher.component';
import { PublisherDetailComponent } from './detail/publisher-detail.component';
import PublisherResolve from './route/publisher-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const publisherRoute: Routes = [
  {
    path: '',
    component: PublisherComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PublisherDetailComponent,
    resolve: {
      publisher: PublisherResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default publisherRoute;
