import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { EducationTestModule } from '../../../test.module';
import { EnrolmentExamComponent } from 'app/entities/enrolment-exam/enrolment-exam.component';
import { EnrolmentExamService } from 'app/entities/enrolment-exam/enrolment-exam.service';
import { EnrolmentExam } from 'app/shared/model/enrolment-exam.model';

describe('Component Tests', () => {
  describe('EnrolmentExam Management Component', () => {
    let comp: EnrolmentExamComponent;
    let fixture: ComponentFixture<EnrolmentExamComponent>;
    let service: EnrolmentExamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EducationTestModule],
        declarations: [EnrolmentExamComponent],
      })
        .overrideTemplate(EnrolmentExamComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnrolmentExamComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EnrolmentExamService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new EnrolmentExam(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.enrolmentExams && comp.enrolmentExams[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
