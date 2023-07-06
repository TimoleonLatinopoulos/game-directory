import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../game-details.test-samples';

import { GameDetailsFormService } from './game-details-form.service';

describe('GameDetails Form Service', () => {
  let service: GameDetailsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(GameDetailsFormService);
  });

  describe('Service methods', () => {
    describe('createGameDetailsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createGameDetailsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            requiredAge: expect.any(Object),
            releaseDate: expect.any(Object),
            pegiRating: expect.any(Object),
            metacriticScore: expect.any(Object),
            imageUrl: expect.any(Object),
            thumbnailUrl: expect.any(Object),
            price: expect.any(Object),
            description: expect.any(Object),
            notes: expect.any(Object),
            steamAppid: expect.any(Object),
            platforms: expect.any(Object),
            developers: expect.any(Object),
            publishers: expect.any(Object),
            categories: expect.any(Object),
          })
        );
      });

      it('passing IGameDetails should create a new form with FormGroup', () => {
        const formGroup = service.createGameDetailsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            requiredAge: expect.any(Object),
            releaseDate: expect.any(Object),
            pegiRating: expect.any(Object),
            metacriticScore: expect.any(Object),
            imageUrl: expect.any(Object),
            thumbnailUrl: expect.any(Object),
            price: expect.any(Object),
            description: expect.any(Object),
            notes: expect.any(Object),
            steamAppid: expect.any(Object),
            platforms: expect.any(Object),
            developers: expect.any(Object),
            publishers: expect.any(Object),
            categories: expect.any(Object),
          })
        );
      });
    });

    describe('getGameDetails', () => {
      it('should return NewGameDetails for default GameDetails initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createGameDetailsFormGroup(sampleWithNewData);

        const gameDetails = service.getGameDetails(formGroup) as any;

        expect(gameDetails).toMatchObject(sampleWithNewData);
      });

      it('should return NewGameDetails for empty GameDetails initial value', () => {
        const formGroup = service.createGameDetailsFormGroup();

        const gameDetails = service.getGameDetails(formGroup) as any;

        expect(gameDetails).toMatchObject({});
      });

      it('should return IGameDetails', () => {
        const formGroup = service.createGameDetailsFormGroup(sampleWithRequiredData);

        const gameDetails = service.getGameDetails(formGroup) as any;

        expect(gameDetails).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IGameDetails should not enable id FormControl', () => {
        const formGroup = service.createGameDetailsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewGameDetails should disable id FormControl', () => {
        const formGroup = service.createGameDetailsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
