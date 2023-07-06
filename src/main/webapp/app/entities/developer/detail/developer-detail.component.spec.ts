import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { DeveloperDetailComponent } from './developer-detail.component';

describe('Developer Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeveloperDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: DeveloperDetailComponent,
              resolve: { developer: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(DeveloperDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load developer on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', DeveloperDetailComponent);

      // THEN
      expect(instance.developer).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
