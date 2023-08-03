import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { UserGameDetailComponent } from './user-game-detail.component';

describe('UserGame Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserGameDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: UserGameDetailComponent,
              resolve: { userGame: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(UserGameDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load userGame on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', UserGameDetailComponent);

      // THEN
      expect(instance.userGame).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
