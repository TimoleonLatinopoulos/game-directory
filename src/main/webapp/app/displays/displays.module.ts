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

@NgModule({
  declarations: [CreateGameEntryComponent],
  imports: [
    RouterModule.forChild(displaysRoute),
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    SharedModule,
    CommonModule,
  ],
})
export class DisplaysModule {}
