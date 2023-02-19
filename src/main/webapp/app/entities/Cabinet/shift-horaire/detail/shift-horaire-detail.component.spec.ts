import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShiftHoraireDetailComponent } from './shift-horaire-detail.component';

describe('ShiftHoraire Management Detail Component', () => {
  let comp: ShiftHoraireDetailComponent;
  let fixture: ComponentFixture<ShiftHoraireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShiftHoraireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ shiftHoraire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ShiftHoraireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ShiftHoraireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load shiftHoraire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.shiftHoraire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
