import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EducationTestModule } from '../../../test.module';
import { EnrolmentCourseDetailComponent } from 'app/entities/enrolment-course/enrolment-course-detail.component';
import { EnrolmentCourse } from 'app/shared/model/enrolment-course.model';

describe('Component Tests', () => {
  describe('EnrolmentCourse Management Detail Component', () => {
    let comp: EnrolmentCourseDetailComponent;
    let fixture: ComponentFixture<EnrolmentCourseDetailComponent>;
    const route = ({ data: of({ enrolmentCourse: new EnrolmentCourse(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [EducationTestModule],
        declarations: [EnrolmentCourseDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(EnrolmentCourseDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EnrolmentCourseDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load enrolmentCourse on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.enrolmentCourse).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
