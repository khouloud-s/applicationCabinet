import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ShiftHoraireFormService } from './shift-horaire-form.service';
import { ShiftHoraireService } from '../service/shift-horaire.service';
import { IShiftHoraire } from '../shift-horaire.model';

import { ShiftHoraireUpdateComponent } from './shift-horaire-update.component';

describe('ShiftHoraire Management Update Component', () => {
  let comp: ShiftHoraireUpdateComponent;
  let fixture: ComponentFixture<ShiftHoraireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let shiftHoraireFormService: ShiftHoraireFormService;
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
    shiftHoraireFormService = TestBed.inject(ShiftHoraireFormService);
    shiftHoraireService = TestBed.inject(ShiftHoraireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const shiftHoraire: IShiftHoraire = { id: 456 };

      activatedRoute.data = of({ shiftHoraire });
      comp.ngOnInit();

      expect(comp.shiftHoraire).toEqual(shiftHoraire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShiftHoraire>>();
      const shiftHoraire = { id: 123 };
      jest.spyOn(shiftHoraireFormService, 'getShiftHoraire').mockReturnValue(shiftHoraire);
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
      expect(shiftHoraireFormService.getShiftHoraire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(shiftHoraireService.update).toHaveBeenCalledWith(expect.objectContaining(shiftHoraire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShiftHoraire>>();
      const shiftHoraire = { id: 123 };
      jest.spyOn(shiftHoraireFormService, 'getShiftHoraire').mockReturnValue({ id: null });
      jest.spyOn(shiftHoraireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ shiftHoraire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: shiftHoraire }));
      saveSubject.complete();

      // THEN
      expect(shiftHoraireFormService.getShiftHoraire).toHaveBeenCalled();
      expect(shiftHoraireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IShiftHoraire>>();
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
      expect(shiftHoraireService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
