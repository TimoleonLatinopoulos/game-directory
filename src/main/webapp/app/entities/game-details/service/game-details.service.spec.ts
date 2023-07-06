import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IGameDetails } from '../game-details.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../game-details.test-samples';

import { GameDetailsService, RestGameDetails } from './game-details.service';

const requireRestSample: RestGameDetails = {
  ...sampleWithRequiredData,
  releaseDate: sampleWithRequiredData.releaseDate?.format(DATE_FORMAT),
};

describe('GameDetails Service', () => {
  let service: GameDetailsService;
  let httpMock: HttpTestingController;
  let expectedResult: IGameDetails | IGameDetails[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(GameDetailsService);
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

    it('should create a GameDetails', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const gameDetails = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(gameDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a GameDetails', () => {
      const gameDetails = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(gameDetails).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a GameDetails', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of GameDetails', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a GameDetails', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGameDetailsToCollectionIfMissing', () => {
      it('should add a GameDetails to an empty array', () => {
        const gameDetails: IGameDetails = sampleWithRequiredData;
        expectedResult = service.addGameDetailsToCollectionIfMissing([], gameDetails);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameDetails);
      });

      it('should not add a GameDetails to an array that contains it', () => {
        const gameDetails: IGameDetails = sampleWithRequiredData;
        const gameDetailsCollection: IGameDetails[] = [
          {
            ...gameDetails,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGameDetailsToCollectionIfMissing(gameDetailsCollection, gameDetails);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a GameDetails to an array that doesn't contain it", () => {
        const gameDetails: IGameDetails = sampleWithRequiredData;
        const gameDetailsCollection: IGameDetails[] = [sampleWithPartialData];
        expectedResult = service.addGameDetailsToCollectionIfMissing(gameDetailsCollection, gameDetails);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameDetails);
      });

      it('should add only unique GameDetails to an array', () => {
        const gameDetailsArray: IGameDetails[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gameDetailsCollection: IGameDetails[] = [sampleWithRequiredData];
        expectedResult = service.addGameDetailsToCollectionIfMissing(gameDetailsCollection, ...gameDetailsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const gameDetails: IGameDetails = sampleWithRequiredData;
        const gameDetails2: IGameDetails = sampleWithPartialData;
        expectedResult = service.addGameDetailsToCollectionIfMissing([], gameDetails, gameDetails2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(gameDetails);
        expect(expectedResult).toContain(gameDetails2);
      });

      it('should accept null and undefined values', () => {
        const gameDetails: IGameDetails = sampleWithRequiredData;
        expectedResult = service.addGameDetailsToCollectionIfMissing([], null, gameDetails, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(gameDetails);
      });

      it('should return initial array if no GameDetails is added', () => {
        const gameDetailsCollection: IGameDetails[] = [sampleWithRequiredData];
        expectedResult = service.addGameDetailsToCollectionIfMissing(gameDetailsCollection, undefined, null);
        expect(expectedResult).toEqual(gameDetailsCollection);
      });
    });

    describe('compareGameDetails', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGameDetails(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGameDetails(entity1, entity2);
        const compareResult2 = service.compareGameDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGameDetails(entity1, entity2);
        const compareResult2 = service.compareGameDetails(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGameDetails(entity1, entity2);
        const compareResult2 = service.compareGameDetails(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
