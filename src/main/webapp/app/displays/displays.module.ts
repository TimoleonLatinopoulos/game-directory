import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CreateGameEntryComponent } from './create-game-entry/create-game-entry.component';
import { RouterModule } from '@angular/router';
import { displaysRoute } from './displays.route';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatTableModule } from '@angular/material/table';
import SharedModule from 'app/shared/shared.module';
import { GamePreviewComponent } from './game-preview/game-preview.component';
import { SeparateCategoriesPipe } from 'app/shared/pipes/separate-categories.pipe';
import { GameListComponent } from './game-list/game-list.component';
import HasAnyAuthorityDirective from 'app/shared/auth/has-any-authority.directive';
import { MAT_DATE_LOCALE } from '@angular/material/core';

@NgModule({
  declarations: [CreateGameEntryComponent, GamePreviewComponent, GameListComponent],
  imports: [
    RouterModule.forChild(displaysRoute),
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    SharedModule,
    CommonModule,
    HasAnyAuthorityDirective,
  ],
  providers: [SeparateCategoriesPipe, { provide: MAT_DATE_LOCALE, useValue: 'en-GB' }],
})
export class DisplaysModule {}
