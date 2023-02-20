import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IShiftHoraire } from '../shift-horaire.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../shift-horaire.test-samples';

import { ShiftHoraireService, RestShiftHoraire } from './shift-horaire.service';

const requireRestSample: RestShiftHoraire = {
  ...sampleWithRequiredData,
  timeStart: sampleWithRequiredData.timeStart?.toJSON(),
  timeEnd: sampleWithRequiredData.timeEnd?.toJSON(),
};

describe('ShiftHoraire Service', () => {
  let service: ShiftHoraireService;
  let httpMock: HttpTestingController;
  let expectedResult: IShiftHoraire | IShiftHoraire[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShiftHoraireService);
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

    it('should create a ShiftHoraire', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const shiftHoraire = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(shiftHoraire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShiftHoraire', () => {
      const shiftHoraire = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(shiftHoraire).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShiftHoraire', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShiftHoraire', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ShiftHoraire', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addShiftHoraireToCollectionIfMissing', () => {
      it('should add a ShiftHoraire to an empty array', () => {
        const shiftHoraire: IShiftHoraire = sampleWithRequiredData;
        expectedResult = service.addShiftHoraireToCollectionIfMissing([], shiftHoraire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shiftHoraire);
      });

      it('should not add a ShiftHoraire to an array that contains it', () => {
        const shiftHoraire: IShiftHoraire = sampleWithRequiredData;
        const shiftHoraireCollection: IShiftHoraire[] = [
          {
            ...shiftHoraire,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, shiftHoraire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShiftHoraire to an array that doesn't contain it", () => {
        const shiftHoraire: IShiftHoraire = sampleWithRequiredData;
        const shiftHoraireCollection: IShiftHoraire[] = [sampleWithPartialData];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, shiftHoraire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shiftHoraire);
      });

      it('should add only unique ShiftHoraire to an array', () => {
        const shiftHoraireArray: IShiftHoraire[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const shiftHoraireCollection: IShiftHoraire[] = [sampleWithRequiredData];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, ...shiftHoraireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shiftHoraire: IShiftHoraire = sampleWithRequiredData;
        const shiftHoraire2: IShiftHoraire = sampleWithPartialData;
        expectedResult = service.addShiftHoraireToCollectionIfMissing([], shiftHoraire, shiftHoraire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shiftHoraire);
        expect(expectedResult).toContain(shiftHoraire2);
      });

      it('should accept null and undefined values', () => {
        const shiftHoraire: IShiftHoraire = sampleWithRequiredData;
        expectedResult = service.addShiftHoraireToCollectionIfMissing([], null, shiftHoraire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shiftHoraire);
      });

      it('should return initial array if no ShiftHoraire is added', () => {
        const shiftHoraireCollection: IShiftHoraire[] = [sampleWithRequiredData];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, undefined, null);
        expect(expectedResult).toEqual(shiftHoraireCollection);
      });
    });

    describe('compareShiftHoraire', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareShiftHoraire(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareShiftHoraire(entity1, entity2);
        const compareResult2 = service.compareShiftHoraire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareShiftHoraire(entity1, entity2);
        const compareResult2 = service.compareShiftHoraire(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareShiftHoraire(entity1, entity2);
        const compareResult2 = service.compareShiftHoraire(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
