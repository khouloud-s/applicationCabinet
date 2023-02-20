import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import dayjs from 'dayjs/esm';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAppointement, Appointement } from '../appointement.model';

import { AppointementService } from './appointement.service';

describe('Appointement Service', () => {
  let service: AppointementService;
  let httpMock: HttpTestingController;
  let elemDefault: IAppointement;
  let expectedResult: IAppointement | IAppointement[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AppointementService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      userUuid: 'AAAAAAA',
      id: 0,
      date: currentDate,
      isActive: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Appointement', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          date: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.create(new Appointement()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Appointement', () => {
      const returnedFromService = Object.assign(
        {
          userUuid: 'BBBBBB',
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          isActive: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Appointement', () => {
      const patchObject = Object.assign({}, new Appointement());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Appointement', () => {
      const returnedFromService = Object.assign(
        {
          userUuid: 'BBBBBB',
          id: 1,
          date: currentDate.format(DATE_FORMAT),
          isActive: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          date: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Appointement', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAppointementToCollectionIfMissing', () => {
      it('should add a Appointement to an empty array', () => {
        const appointement: IAppointement = { id: 123 };
        expectedResult = service.addAppointementToCollectionIfMissing([], appointement);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appointement);
      });

      it('should not add a Appointement to an array that contains it', () => {
        const appointement: IAppointement = { id: 123 };
        const appointementCollection: IAppointement[] = [
          {
            ...appointement,
          },
          { id: 456 },
        ];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, appointement);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Appointement to an array that doesn't contain it", () => {
        const appointement: IAppointement = { id: 123 };
        const appointementCollection: IAppointement[] = [{ id: 456 }];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, appointement);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appointement);
      });

      it('should add only unique Appointement to an array', () => {
        const appointementArray: IAppointement[] = [{ id: 123 }, { id: 456 }, { id: 99504 }];
        const appointementCollection: IAppointement[] = [{ id: 123 }];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, ...appointementArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const appointement: IAppointement = { id: 123 };
        const appointement2: IAppointement = { id: 456 };
        expectedResult = service.addAppointementToCollectionIfMissing([], appointement, appointement2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(appointement);
        expect(expectedResult).toContain(appointement2);
      });

      it('should accept null and undefined values', () => {
        const appointement: IAppointement = { id: 123 };
        expectedResult = service.addAppointementToCollectionIfMissing([], null, appointement, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(appointement);
      });

      it('should return initial array if no Appointement is added', () => {
        const appointementCollection: IAppointement[] = [{ id: 123 }];
        expectedResult = service.addAppointementToCollectionIfMissing(appointementCollection, undefined, null);
        expect(expectedResult).toEqual(appointementCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
