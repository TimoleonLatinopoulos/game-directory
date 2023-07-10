import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import SharedModule from './shared.module';

@NgModule({
  exports: [FormsModule, CommonModule, NgbModule, FontAwesomeModule, ReactiveFormsModule, InfiniteScrollModule, SharedModule],
})
export class SharedLibsModule {}
