import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { RoleName } from 'app/entities/enumerations/role-name.model';
import { IRole, Role } from '../role.model';

import { RoleService } from './role.service';

describe('Role Service', () => {
  let service: RoleService;
  let httpMock: HttpTestingController;
  let elemDefault: IRole;
  let expectedResult: IRole | IRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(RoleService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      userUuid: 'AAAAAAA',
      id: 0,
      rolename: RoleName.ROLE_MEDECIN,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Role', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Role()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Role', () => {
      const returnedFromService = Object.assign(
        {
          userUuid: 'BBBBBB',
          id: 1,
          rolename: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Role', () => {
      const patchObject = Object.assign(
        {
          userUuid: 'BBBBBB',
          rolename: 'BBBBBB',
        },
        new Role()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Role', () => {
      const returnedFromService = Object.assign(
        {
          userUuid: 'BBBBBB',
          id: 1,
          rolename: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Role', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addRoleToCollectionIfMissing', () => {
      it('should add a Role to an empty array', () => {
        const role: IRole = { id: 123 };
        expectedResult = service.addRoleToCollectionIfMissing([], role);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(role);
      });

      it('should not add a Role to an array that contains it', () => {
        const role: IRole = { id: 123 };
        const roleCollection: IRole[] = [
          {
            ...role,
          },
          { id: 456 },
        ];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, role);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Role to an array that doesn't contain it", () => {
        const role: IRole = { id: 123 };
        const roleCollection: IRole[] = [{ id: 456 }];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, role);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(role);
      });

      it('should add only unique Role to an array', () => {
        const roleArray: IRole[] = [{ id: 123 }, { id: 456 }, { id: 74176 }];
        const roleCollection: IRole[] = [{ id: 123 }];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, ...roleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const role: IRole = { id: 123 };
        const role2: IRole = { id: 456 };
        expectedResult = service.addRoleToCollectionIfMissing([], role, role2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(role);
        expect(expectedResult).toContain(role2);
      });

      it('should accept null and undefined values', () => {
        const role: IRole = { id: 123 };
        expectedResult = service.addRoleToCollectionIfMissing([], null, role, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(role);
      });

      it('should return initial array if no Role is added', () => {
        const roleCollection: IRole[] = [{ id: 123 }];
        expectedResult = service.addRoleToCollectionIfMissing(roleCollection, undefined, null);
        expect(expectedResult).toEqual(roleCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
