import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IShiftHoraire, ShiftHoraire } from '../shift-horaire.model';

import { ShiftHoraireService } from './shift-horaire.service';

describe('ShiftHoraire Service', () => {
  let service: ShiftHoraireService;
  let httpMock: HttpTestingController;
  let elemDefault: IShiftHoraire;
  let expectedResult: IShiftHoraire | IShiftHoraire[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ShiftHoraireService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      userUuid: 'AAAAAAA',
      id: 0,
      timeStart: currentDate,
      timeEnd: currentDate,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a ShiftHoraire', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.create(new ShiftHoraire()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ShiftHoraire', () => {
      const returnedFromService = Object.assign(
        {
          userUuid: 'BBBBBB',
          id: 1,
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ShiftHoraire', () => {
      const patchObject = Object.assign(
        {
          timeStart: currentDate.format(DATE_TIME_FORMAT),
        },
        new ShiftHoraire()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ShiftHoraire', () => {
      const returnedFromService = Object.assign(
        {
          userUuid: 'BBBBBB',
          id: 1,
          timeStart: currentDate.format(DATE_TIME_FORMAT),
          timeEnd: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          timeStart: currentDate,
          timeEnd: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a ShiftHoraire', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addShiftHoraireToCollectionIfMissing', () => {
      it('should add a ShiftHoraire to an empty array', () => {
        const shiftHoraire: IShiftHoraire = { id: 123 };
        expectedResult = service.addShiftHoraireToCollectionIfMissing([], shiftHoraire);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shiftHoraire);
      });

      it('should not add a ShiftHoraire to an array that contains it', () => {
        const shiftHoraire: IShiftHoraire = { id: 123 };
        const shiftHoraireCollection: IShiftHoraire[] = [
          {
            ...shiftHoraire,
          },
          { id: 456 },
        ];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, shiftHoraire);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ShiftHoraire to an array that doesn't contain it", () => {
        const shiftHoraire: IShiftHoraire = { id: 123 };
        const shiftHoraireCollection: IShiftHoraire[] = [{ id: 456 }];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, shiftHoraire);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shiftHoraire);
      });

      it('should add only unique ShiftHoraire to an array', () => {
        const shiftHoraireArray: IShiftHoraire[] = [{ id: 123 }, { id: 456 }, { id: 83271 }];
        const shiftHoraireCollection: IShiftHoraire[] = [{ id: 123 }];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, ...shiftHoraireArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const shiftHoraire: IShiftHoraire = { id: 123 };
        const shiftHoraire2: IShiftHoraire = { id: 456 };
        expectedResult = service.addShiftHoraireToCollectionIfMissing([], shiftHoraire, shiftHoraire2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(shiftHoraire);
        expect(expectedResult).toContain(shiftHoraire2);
      });

      it('should accept null and undefined values', () => {
        const shiftHoraire: IShiftHoraire = { id: 123 };
        expectedResult = service.addShiftHoraireToCollectionIfMissing([], null, shiftHoraire, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(shiftHoraire);
      });

      it('should return initial array if no ShiftHoraire is added', () => {
        const shiftHoraireCollection: IShiftHoraire[] = [{ id: 123 }];
        expectedResult = service.addShiftHoraireToCollectionIfMissing(shiftHoraireCollection, undefined, null);
        expect(expectedResult).toEqual(shiftHoraireCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
