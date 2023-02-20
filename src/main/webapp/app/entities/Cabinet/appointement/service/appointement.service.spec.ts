import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAppointement } from '../appointement.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../appointement.test-samples';

import { AppointementService, RestAppointement } from './appointement.service';

const requireRestSample: RestAppointement = {
  ...sampleWithRequiredData,
  date: sampleWithRequiredData.date?.format(DATE_FORMAT),
};

describe('Appointement Service', () => {
  let service: AppointementService;
  let httpMock: HttpTestingController;
  let expectedResult: IAppointement | IAppointement[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppointementService);
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

    it('should create a Appointement', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const appointement = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(appointement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Appointement', () => {
      const appointement = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(appointement).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Appointement', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Appointement', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Appointement', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addAppointementToCollectionIfMissing', () => {
      it('should add a Appointement to an empty array', () => {
        const appointement: IAppointement = sampleWithRequiredData;
        expectedResult = service.addAppointementToCollectionIfMissing([], appointement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appointement);
      });

      it('should not add a Appointement to an array that contains it', () => {
        const appointement: IAppointement = sampleWithRequiredData;
        const appointementCollection: IAppointement[] = [
          {
            ...appointement,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, appointement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Appointement to an array that doesn't contain it", () => {
        const appointement: IAppointement = sampleWithRequiredData;
        const appointementCollection: IAppointement[] = [sampleWithPartialData];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, appointement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appointement);
      });

      it('should add only unique Appointement to an array', () => {
        const appointementArray: IAppointement[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const appointementCollection: IAppointement[] = [sampleWithRequiredData];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, ...appointementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appointement: IAppointement = sampleWithRequiredData;
        const appointement2: IAppointement = sampleWithPartialData;
        expectedResult = service.addAppointementToCollectionIfMissing([], appointement, appointement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appointement);
        expect(expectedResult).toContain(appointement2);
      });

      it('should accept null and undefined values', () => {
        const appointement: IAppointement = sampleWithRequiredData;
        expectedResult = service.addAppointementToCollectionIfMissing([], null, appointement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appointement);
      });

      it('should return initial array if no Appointement is added', () => {
        const appointementCollection: IAppointement[] = [sampleWithRequiredData];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, undefined, null);
        expect(expectedResult).toEqual(appointementCollection);
      });
    });

    describe('compareAppointement', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareAppointement(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareAppointement(entity1, entity2);
        const compareResult2 = service.compareAppointement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareAppointement(entity1, entity2);
        const compareResult2 = service.compareAppointement(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareAppointement(entity1, entity2);
        const compareResult2 = service.compareAppointement(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
