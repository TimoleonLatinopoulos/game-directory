import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IUserGame } from '../user-game.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../user-game.test-samples';

import { UserGameService, RestUserGame } from './user-game.service';

const requireRestSample: RestUserGame = {
  ...sampleWithRequiredData,
  dateAdded: sampleWithRequiredData.dateAdded?.format(DATE_FORMAT),
};

describe('UserGame Service', () => {
  let service: UserGameService;
  let httpMock: HttpTestingController;
  let expectedResult: IUserGame | IUserGame[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(UserGameService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a UserGame', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const userGame = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(userGame).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a UserGame', () => {
      const userGame = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(userGame).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a UserGame', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of UserGame', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a UserGame', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addUserGameToCollectionIfMissing', () => {
      it('should add a UserGame to an empty array', () => {
        const userGame: IUserGame = sampleWithRequiredData;
        expectedResult = service.addUserGameToCollectionIfMissing([], userGame);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userGame);
      });

      it('should not add a UserGame to an array that contains it', () => {
        const userGame: IUserGame = sampleWithRequiredData;
        const userGameCollection: IUserGame[] = [
          {
            ...userGame,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addUserGameToCollectionIfMissing(userGameCollection, userGame);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a UserGame to an array that doesn't contain it", () => {
        const userGame: IUserGame = sampleWithRequiredData;
        const userGameCollection: IUserGame[] = [sampleWithPartialData];
        expectedResult = service.addUserGameToCollectionIfMissing(userGameCollection, userGame);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userGame);
      });

      it('should add only unique UserGame to an array', () => {
        const userGameArray: IUserGame[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const userGameCollection: IUserGame[] = [sampleWithRequiredData];
        expectedResult = service.addUserGameToCollectionIfMissing(userGameCollection, ...userGameArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const userGame: IUserGame = sampleWithRequiredData;
        const userGame2: IUserGame = sampleWithPartialData;
        expectedResult = service.addUserGameToCollectionIfMissing([], userGame, userGame2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(userGame);
        expect(expectedResult).toContain(userGame2);
      });

      it('should accept null and undefined values', () => {
        const userGame: IUserGame = sampleWithRequiredData;
        expectedResult = service.addUserGameToCollectionIfMissing([], null, userGame, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(userGame);
      });

      it('should return initial array if no UserGame is added', () => {
        const userGameCollection: IUserGame[] = [sampleWithRequiredData];
        expectedResult = service.addUserGameToCollectionIfMissing(userGameCollection, undefined, null);
        expect(expectedResult).toEqual(userGameCollection);
      });
    });

    describe('compareUserGame', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareUserGame(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareUserGame(entity1, entity2);
        const compareResult2 = service.compareUserGame(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareUserGame(entity1, entity2);
        const compareResult2 = service.compareUserGame(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareUserGame(entity1, entity2);
        const compareResult2 = service.compareUserGame(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
