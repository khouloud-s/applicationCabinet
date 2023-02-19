import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ShiftHoraireService } from '../service/shift-horaire.service';
import { IShiftHoraire, ShiftHoraire } from '../shift-horaire.model';

import { ShiftHoraireUpdateComponent } from './shift-horaire-update.component';

describe('ShiftHoraire Management Update Component', () => {
  let comp: ShiftHoraireUpdateComponent;
  let fixture: ComponentFixture<ShiftHoraireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shiftHoraireService: ShiftHoraireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ShiftHoraireUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(ShiftHoraireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShiftHoraireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    shiftHoraireService = TestBed.inject(ShiftHoraireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const shiftHoraire: IShiftHoraire = { id: 456 };

      activatedRoute.data = of({ shiftHoraire });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(shiftHoraire));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShiftHoraire>>();
      const shiftHoraire = { id: 123 };
      jest.spyOn(shiftHoraireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shiftHoraire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shiftHoraire }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(shiftHoraireService.update).toHaveBeenCalledWith(shiftHoraire);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShiftHoraire>>();
      const shiftHoraire = new ShiftHoraire();
      jest.spyOn(shiftHoraireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shiftHoraire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shiftHoraire }));
      saveSubject.complete();

      // THEN
      expect(shiftHoraireService.create).toHaveBeenCalledWith(shiftHoraire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ShiftHoraire>>();
      const shiftHoraire = { id: 123 };
      jest.spyOn(shiftHoraireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shiftHoraire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(shiftHoraireService.update).toHaveBeenCalledWith(shiftHoraire);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
