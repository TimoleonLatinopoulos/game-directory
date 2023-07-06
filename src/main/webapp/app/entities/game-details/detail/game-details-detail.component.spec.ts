import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { GameDetailsDetailComponent } from './game-details-detail.component';

describe('GameDetails Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GameDetailsDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: GameDetailsDetailComponent,
              resolve: { gameDetails: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding()
        ),
      ],
    })
      .overrideTemplate(GameDetailsDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load gameDetails on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', GameDetailsDetailComponent);

      // THEN
      expect(instance.gameDetails).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
