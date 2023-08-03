import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../user-game.test-samples';

import { UserGameFormService } from './user-game-form.service';

describe('UserGame Form Service', () => {
  let service: UserGameFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserGameFormService);
  });

  describe('Service methods', () => {
    describe('createUserGameFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createUserGameFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            favourite: expect.any(Object),
            dateAdded: expect.any(Object),
            title: expect.any(Object),
            user: expect.any(Object),
            game: expect.any(Object),
          })
        );
      });

      it('passing IUserGame should create a new form with FormGroup', () => {
        const formGroup = service.createUserGameFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            favourite: expect.any(Object),
            dateAdded: expect.any(Object),
            title: expect.any(Object),
            user: expect.any(Object),
            game: expect.any(Object),
          })
        );
      });
    });

    describe('getUserGame', () => {
      it('should return NewUserGame for default UserGame initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createUserGameFormGroup(sampleWithNewData);

        const userGame = service.getUserGame(formGroup) as any;

        expect(userGame).toMatchObject(sampleWithNewData);
      });

      it('should return NewUserGame for empty UserGame initial value', () => {
        const formGroup = service.createUserGameFormGroup();

        const userGame = service.getUserGame(formGroup) as any;

        expect(userGame).toMatchObject({});
      });

      it('should return IUserGame', () => {
        const formGroup = service.createUserGameFormGroup(sampleWithRequiredData);

        const userGame = service.getUserGame(formGroup) as any;

        expect(userGame).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IUserGame should not enable id FormControl', () => {
        const formGroup = service.createUserGameFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewUserGame should disable id FormControl', () => {
        const formGroup = service.createUserGameFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
