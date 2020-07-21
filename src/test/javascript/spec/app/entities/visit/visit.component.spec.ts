import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { MentalCentralTestModule } from '../../../test.module';
import { VisitComponent } from 'app/entities/visit/visit.component';
import { VisitService } from 'app/entities/visit/visit.service';
import { Visit } from 'app/shared/model/visit.model';

describe('Component Tests', () => {
  describe('Visit Management Component', () => {
    let comp: VisitComponent;
    let fixture: ComponentFixture<VisitComponent>;
    let service: VisitService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [MentalCentralTestModule],
        declarations: [VisitComponent],
      })
        .overrideTemplate(VisitComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VisitComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(VisitService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new Visit(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.visits && comp.visits[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
