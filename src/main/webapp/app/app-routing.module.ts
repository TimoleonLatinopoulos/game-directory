import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { DEBUG_INFO_ENABLED } from 'app/app.constants';
import { Authority } from 'app/config/authority.constants';

import NavbarComponent from './layouts/navbar/navbar.component';
import LoginComponent from './login/login.component';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DisplaysModule } from './displays/displays.module';
import FooterComponent from './layouts/footer/footer.component';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: '',
          component: NavbarComponent,
          outlet: 'navbar',
        },
        {
          path: '',
          component: FooterComponent,
          outlet: 'footer',
        },
        {
          path: 'admin',
          data: {
            authorities: [Authority.ADMIN],
          },
          canActivate: [UserRouteAccessService],
          loadChildren: () => import('./admin/admin-routing.module'),
        },
        {
          path: 'account',
          loadChildren: () => import('./account/account.route'),
        },
        {
          path: 'login',
          component: LoginComponent,
          title: 'Sign in',
        },
        {
          path: '',
          loadChildren: () => import(`./entities/entity-routing.module`).then(({ EntityRoutingModule }) => EntityRoutingModule),
        },
        ...errorRoute,
      ],
      { enableTracing: DEBUG_INFO_ENABLED, bindToComponentInputs: true }
    ),
    DisplaysModule,
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
