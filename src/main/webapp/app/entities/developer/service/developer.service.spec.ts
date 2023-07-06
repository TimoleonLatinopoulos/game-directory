import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDeveloper } from '../developer.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../developer.test-samples';

import { DeveloperService } from './developer.service';

const requireRestSample: IDeveloper = {
  ...sampleWithRequiredData,
};

describe('Developer Service', () => {
  let service: DeveloperService;
  let httpMock: HttpTestingController;
  let expectedResult: IDeveloper | IDeveloper[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DeveloperService);
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

    it('should return a list of Developer', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    describe('addDeveloperToCollectionIfMissing', () => {
      it('should add a Developer to an empty array', () => {
        const developer: IDeveloper = sampleWithRequiredData;
        expectedResult = service.addDeveloperToCollectionIfMissing([], developer);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(developer);
      });

      it('should not add a Developer to an array that contains it', () => {
        const developer: IDeveloper = sampleWithRequiredData;
        const developerCollection: IDeveloper[] = [
          {
            ...developer,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDeveloperToCollectionIfMissing(developerCollection, developer);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Developer to an array that doesn't contain it", () => {
        const developer: IDeveloper = sampleWithRequiredData;
        const developerCollection: IDeveloper[] = [sampleWithPartialData];
        expectedResult = service.addDeveloperToCollectionIfMissing(developerCollection, developer);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(developer);
      });

      it('should add only unique Developer to an array', () => {
        const developerArray: IDeveloper[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const developerCollection: IDeveloper[] = [sampleWithRequiredData];
        expectedResult = service.addDeveloperToCollectionIfMissing(developerCollection, ...developerArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const developer: IDeveloper = sampleWithRequiredData;
        const developer2: IDeveloper = sampleWithPartialData;
        expectedResult = service.addDeveloperToCollectionIfMissing([], developer, developer2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(developer);
        expect(expectedResult).toContain(developer2);
      });

      it('should accept null and undefined values', () => {
        const developer: IDeveloper = sampleWithRequiredData;
        expectedResult = service.addDeveloperToCollectionIfMissing([], null, developer, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(developer);
      });

      it('should return initial array if no Developer is added', () => {
        const developerCollection: IDeveloper[] = [sampleWithRequiredData];
        expectedResult = service.addDeveloperToCollectionIfMissing(developerCollection, undefined, null);
        expect(expectedResult).toEqual(developerCollection);
      });
    });

    describe('compareDeveloper', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDeveloper(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareDeveloper(entity1, entity2);
        const compareResult2 = service.compareDeveloper(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareDeveloper(entity1, entity2);
        const compareResult2 = service.compareDeveloper(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareDeveloper(entity1, entity2);
        const compareResult2 = service.compareDeveloper(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
