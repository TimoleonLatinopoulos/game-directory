import { Component } from '@angular/core';
import SharedModule from 'app/shared/shared.module';

@Component({
  standalone: true,
  selector: 'jhi-footer',
  templateUrl: './footer.component.html',
  imports: [SharedModule],
})
export default class FooterComponent {}
