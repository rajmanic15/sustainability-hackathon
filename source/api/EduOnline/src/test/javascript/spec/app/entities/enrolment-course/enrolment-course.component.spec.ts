import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { EducationTestModule } from '../../../test.module';
import { EnrolmentCourseComponent } from 'app/entities/enrolment-course/enrolment-course.component';
import { EnrolmentCourseService } from 'app/entities/enrolment-course/enrolment-course.service';
import { EnrolmentCourse } from 'app/shared/model/enrolment-course.model';

describe('Component Tests', () => {
  describe('EnrolmentCourse Management Component', () => {
    let comp: EnrolmentCourseComponent;
    let fixture: ComponentFixture<EnrolmentCourseComponent>;
    let service: EnrolmentCourseService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EducationTestModule],
        declarations: [EnrolmentCourseComponent],
      })
        .overrideTemplate(EnrolmentCourseComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EnrolmentCourseComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(EnrolmentCourseService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new EnrolmentCourse(123)],
            headers,
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.enrolmentCourses && comp.enrolmentCourses[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
