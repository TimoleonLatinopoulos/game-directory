import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { PlatformDetailComponent } from './platform-detail.component';

describe('Platform Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PlatformDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: PlatformDetailComponent,
              resolve: { platform: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(PlatformDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load platform on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', PlatformDetailComponent);

      // THEN
      expect(instance.platform).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
