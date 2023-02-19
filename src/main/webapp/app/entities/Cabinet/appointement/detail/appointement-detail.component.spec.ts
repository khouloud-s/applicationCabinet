import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AppointementDetailComponent } from './appointement-detail.component';

describe('Appointement Management Detail Component', () => {
  let comp: AppointementDetailComponent;
  let fixture: ComponentFixture<AppointementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AppointementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ appointement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AppointementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AppointementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load appointement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.appointement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
